package com.team4.lib.trajectory;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.physics.DifferentialDrive;
import com.team254.lib.trajectory.TrajectoryIterator;
import com.team254.lib.trajectory.TrajectorySamplePoint;
import com.team254.lib.trajectory.timing.TimedState;
import com.team254.lib.util.Util;
import com.team4.lib.util.DriveSpeed;

import edu.wpi.first.math.util.Units;

public class DrivePathPlanner {

    DifferentialDrive mDriveModel;

    TrajectoryIterator<TimedState<Pose2dWithCurvature>> mCurrentTrajectory;
    boolean mIsReversed = false;
    double mLastTime = Double.POSITIVE_INFINITY;
    public TimedState<Pose2dWithCurvature> mSetpoint = new TimedState<>(Pose2dWithCurvature.identity());
    Pose2d mError = Pose2d.identity();
    DriveSpeed mSpeed = new DriveSpeed();

    DifferentialDrive.ChassisState prevVelocity = new DifferentialDrive.ChassisState();
    double mDt = 0.0;
    
    public DrivePathPlanner(DifferentialDrive drive)
    {
        mDriveModel = drive;
    }

    public void setTrajectory(final TrajectoryIterator<TimedState<Pose2dWithCurvature>> trajectory) {
        mCurrentTrajectory = trajectory;
        mSetpoint = trajectory.getState();
        for (int i = 0; i < trajectory.trajectory().length(); ++i) {
            if (trajectory.trajectory().getState(i).velocity() > Util.kEpsilon) {
                mIsReversed = false;
                break;
            } else if (trajectory.trajectory().getState(i).velocity() < -Util.kEpsilon) {
                mIsReversed = true;
                break;
            }
        }
    }

    public void reset() {
        mError = Pose2d.identity();
        mSpeed = new DriveSpeed();
        mLastTime = Double.POSITIVE_INFINITY;
    }


    // The current Path Follower uses the ramsete controller from WPI (which is based off 254's 2020 path follower)
    public DriveSpeed calculate(double timestamp, Pose2d state)
    {
        if (mCurrentTrajectory == null) return new DriveSpeed();

        if (mCurrentTrajectory.getProgress() == 0.0 && !Double.isFinite(mLastTime)) {
            mLastTime = timestamp;
        }

        mDt = timestamp - mLastTime;
        mLastTime = timestamp;
        TrajectorySamplePoint<TimedState<Pose2dWithCurvature>> sample_point = mCurrentTrajectory.advance(mDt);
        mSetpoint = sample_point.state();

        if (!mCurrentTrajectory.isDone()) {
            // Generate feedforward voltages.
            final double velocity_m = Units.inchesToMeters(mSetpoint.velocity());
            final double curvature_m = Units.metersToInches(mSetpoint.state().getCurvature());
            final double dcurvature_ds_m = Units.metersToInches(Units.metersToInches(mSetpoint.state()
                    .getDCurvatureDs()));
            final double acceleration_m = Units.inchesToMeters(mSetpoint.acceleration());
            final DifferentialDrive.DriveDynamics dynamics = mDriveModel.solveInverseDynamics(
                    new DifferentialDrive.ChassisState(velocity_m, velocity_m * curvature_m),
                    new DifferentialDrive.ChassisState(acceleration_m,
                            acceleration_m * curvature_m + velocity_m * velocity_m * dcurvature_ds_m));
            mError = state.inverse().transformBy(mSetpoint.state().getPose());

           mSpeed = ramseteCalculate(dynamics, state);
        } else {
            mSpeed = new DriveSpeed();
        }
        return mSpeed;
    }

    public DriveSpeed ramseteCalculate(DifferentialDrive.DriveDynamics dynamics, Pose2d state)
    {
        double kBeta = 1.5;  // >0.
        double kZeta = 0.7;  // Damping coefficient, [0, 1].

        // Compute gain parameter.
        final double k = 2.0 * kZeta * Math.sqrt(kBeta * dynamics.chassis_velocity.linear * dynamics.chassis_velocity
                .linear + dynamics.chassis_velocity.angular * dynamics.chassis_velocity.angular);

        // Compute error components.
        final double angle_error_rads = mError.getRotation().getRadians();
        final double sin_x_over_x = Util.epsilonEquals(angle_error_rads, 0.0, 1E-2) ?
                1.0 : mError.getRotation().sin() / angle_error_rads;
        final DifferentialDrive.ChassisState adjusted_velocity = new DifferentialDrive.ChassisState(
                dynamics.chassis_velocity.linear * mError.getRotation().cos() +
                        k * Units.inchesToMeters(mError.getTranslation().x()),
                dynamics.chassis_velocity.angular + k * angle_error_rads +
                        dynamics.chassis_velocity.linear * kBeta * sin_x_over_x * Units.inchesToMeters(mError
                                .getTranslation().y()));

        // Compute adjusted left and right wheel velocities.
        dynamics.chassis_velocity = adjusted_velocity;
        dynamics.wheel_velocity = mDriveModel.solveInverseKinematics(adjusted_velocity);

        dynamics.chassis_acceleration.linear = mDt == 0 ? 0.0 : (dynamics.chassis_velocity.linear - prevVelocity
                .linear) / mDt;
        dynamics.chassis_acceleration.angular = mDt == 0 ? 0.0 : (dynamics.chassis_velocity.angular - prevVelocity
                .angular) / mDt;

        prevVelocity = dynamics.chassis_velocity;

        DifferentialDrive.WheelState feedforward_voltages = mDriveModel.solveInverseDynamics(dynamics.chassis_velocity,
                dynamics.chassis_acceleration).voltage;

        return new DriveSpeed(dynamics.wheel_velocity.left, dynamics.wheel_velocity.right, feedforward_voltages.left, feedforward_voltages.right);
    }

}
