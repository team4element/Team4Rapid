package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.DemandType;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.team254.lib.control.Lookahead;
import com.team254.lib.control.Path;
import com.team254.lib.control.PathFollower;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.geometry.Twist2d;
import com.team254.lib.util.DriveSignal;
import com.team4.lib.drivers.LazyTalonFX;
import com.team4.lib.drivers.NavX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.lib.drivers.TalonUtil;
import com.team4.lib.util.ElementMath;
import com.team4.lib.util.FieldState;
import com.team4.lib.util.Kinematics;
import com.team4.robot.Constants;
import com.team4.robot.Robot;

public class Drive extends Subsystem {

	// Motors that controls wheels
	private final LazyTalonFX mLeftMaster1, mleftFollower2;
	private final LazyTalonFX mRightMaster1, mRightFollower2;
	private NavX mNavX;

	private driveState state;

	double mLeftPositionInches = 0d;
	double mRightPositionInches = 0d;
	double mAngleDegrees = 0d;
	Rotation2d mAngle = null;

	double mLeftVelocity = 0d;
	double mRightVelocity = 0d;

	private PathFollower mPathFollower;
    private Path mCurrentPath = null;

	public Drive() {
		// Starts all Talons in Coast Mode
		mLeftMaster1 = TalonFactory.createDefaultTalonFX(Constants.kDriveLeftMaster1);
		configureTalonFX(mLeftMaster1, true, true);

		mleftFollower2 = TalonFactory.createPermanentSlaveTalonFX(
			Constants.kDriveLeftFollower2, mLeftMaster1);
		configureTalonFX(mleftFollower2, true, false);

		mRightMaster1 = TalonFactory.createDefaultTalonFX(Constants.kDriveRightMaster1);
		configureTalonFX(mRightMaster1, false, true);

		mRightFollower2 = TalonFactory.createPermanentSlaveTalonFX(
			Constants.kDriveRightFollower2, mRightMaster1);
		configureTalonFX(mRightFollower2, false, false);

		mNavX = new NavX();

		setCoastMode();
		resetSensors();

		reloadGains();
	}


	@Override
	public void readPeriodicInputs() {
		// System.out.println("Current Angle: " + getAngleDegrees());



		mLeftPositionInches = ElementMath.rotationsToInches(
			ElementMath.ticksToRotations(mLeftMaster1.getSelectedSensorPosition(), 
			Constants.kDriveEnconderPPR), 
			Constants.kDriveWheelCircumferenceInches, 
			Constants.kDriveGearRatio);
		
		mRightPositionInches = ElementMath.rotationsToInches(
			ElementMath.ticksToRotations(mRightMaster1.getSelectedSensorPosition(), 
			Constants.kDriveEnconderPPR), 
			Constants.kDriveWheelCircumferenceInches,
			Constants.kDriveGearRatio);
		

		mAngleDegrees = mNavX.getHeadingDegrees();
		mAngle = mNavX.getHeading();

		mLeftVelocity = 
			ElementMath.rotationsToInches(
				ElementMath.tickPer100msToRPM(mLeftMaster1.getSelectedSensorVelocity(0), 
											Constants.kDriveEnconderPPR), 
				Constants.kDriveWheelCircumferenceInches, 
				Constants.kDriveGearRatio) / 60.0;
			
		mRightVelocity = 
				ElementMath.rotationsToInches(
					ElementMath.tickPer100msToRPM(mRightMaster1.getSelectedSensorVelocity(0), 
												Constants.kDriveEnconderPPR), 
					Constants.kDriveWheelCircumferenceInches, 
					Constants.kDriveGearRatio) / 60.0;

		// System.out.println("Distnace: " + getDistance());
	}

	@Override
	public synchronized void writePeriodicOutputs() {
	}

	@Override
	public void onDisableLoop() {
		setCoastMode();
	}

	@Override
	public void onLoop(double timestamp) {
	}

	public static void configureTalonFX(LazyTalonFX talon, boolean left, boolean main_encoder_talon) {
		talon.setInverted(!left);

		if (main_encoder_talon) {
			TalonUtil.checkError(talon.setStatusFramePeriod(
				StatusFrame.Status_2_Feedback0, 20, Constants.kLongCANTimeoutMs),
					"could not set drive feedback frame");
			TalonUtil.checkError(
					talon.configSelectedFeedbackSensor(
						TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.kLongCANTimeoutMs),
					"could not detect motor encoder");
			talon.setSensorPhase(true);
		}
	}
	
	public synchronized void setCoastMode() {
		mLeftMaster1.setNeutralMode(NeutralMode.Coast);
		mleftFollower2.setNeutralMode(NeutralMode.Coast);
		mRightMaster1.setNeutralMode(NeutralMode.Coast);
		mRightFollower2.setNeutralMode(NeutralMode.Coast);
	}

	public synchronized void setBrakeMode() {
		mLeftMaster1.setNeutralMode(NeutralMode.Brake);
		mleftFollower2.setNeutralMode(NeutralMode.Brake);
		mRightMaster1.setNeutralMode(NeutralMode.Brake);
		mRightFollower2.setNeutralMode(NeutralMode.Brake);
	}

	private void configureOpenTalon() {
		setCoastMode();
		mLeftMaster1.configOpenloopRamp(0.25);
		mRightMaster1.configOpenloopRamp(0.25);
		state = driveState.OPEN;
	}

	private void configureVelocityTalon() {
		setCoastMode();
		mLeftMaster1.configOpenloopRamp(0.0);
		mRightMaster1.configOpenloopRamp(0.0);
		state = driveState.VELOCITY;
	}


	public synchronized void setOpenLoop(DriveSignal signal) {
		if (state != driveState.OPEN) {
			configureOpenTalon();
		}
		mLeftMaster1.set(ControlMode.PercentOutput, signal.getLeft());
		mRightMaster1.set(ControlMode.PercentOutput, signal.getRight());
	}

	public synchronized void setVelocity(DriveSignal velocity, DriveSignal ff) {
		if (state != driveState.VELOCITY) {
			configureVelocityTalon();
		}
		mLeftMaster1.set(ControlMode.Velocity, velocity.getLeft(), DemandType.ArbitraryFeedForward, ff.getLeft());
		mRightMaster1.set(ControlMode.Velocity, velocity.getRight(), DemandType.ArbitraryFeedForward, ff.getRight());
	}

	public synchronized void setPath(Path path, boolean reversed)
	{
		if (mCurrentPath != path) {
            Robot.mFieldState.resetDistanceDriven();
            mPathFollower = new PathFollower(path, reversed, new PathFollower.Parameters(
                    new Lookahead(Constants.kMinLookAhead, Constants.kMaxLookAhead, Constants.kMinLookAheadSpeed,
                            Constants.kMaxLookAheadSpeed),
                    Constants.kInertiaSteeringGain, Constants.kPathFollowingProfileKp,
                    Constants.kPathFollowingProfileKi, Constants.kPathFollowingProfileKv,
                    Constants.kPathFollowingProfileKffv, Constants.kPathFollowingProfileKffa,
                    Constants.kPathFollowingProfileKs, Constants.kPathFollowingMaxVel,
                    Path.kPathFollowingMaxAccel, Constants.kPathFollowingGoalPosTolerance,
                    Constants.kPathFollowingGoalVelTolerance, Constants.kPathStopSteeringDistance));
            mCurrentPath = path;
        } else {
            setVelocity(new DriveSignal(0, 0), new DriveSignal(0, 0));
        }
	}

	public void updatePathFollower(double timestamp)
	{
		if (mCurrentPath != null)
		{
			FieldState fieldState = Robot.mFieldState;
            Pose2d fieldToVehicle = fieldState.getLatestFieldToVehicle().getValue();
            Twist2d currentSpeed = mPathFollower.calculate(timestamp, fieldToVehicle, fieldState.getDistanceDriven(),
                    fieldState.getPredictedVelocity().dx);
            if (!mPathFollower.isFinished()) {

                
                DriveSignal setpoint = Kinematics.inverseKinematics(currentSpeed);
                setOpenLoop(setpoint);
            } else {
                if (!mPathFollower.isForceFinished()) {
                    setVelocity(new DriveSignal(0, 0), new DriveSignal(0, 0));
                }
            }
		}
		else
		{
			setOpenLoop(DriveSignal.NEUTRAL);
		}
	}

	public boolean isDoneWithTrajectory() {
		return mPathFollower.isFinished();
	}

	public synchronized boolean hasPassedMarker(String marker) {
        if (mPathFollower != null) {
            return mPathFollower.hasPassedMarker(marker);
        } else {
            return false;
        }
    }

	public synchronized void forceFinishPath()
	{
		if (mPathFollower != null)
		{
			mPathFollower.forceFinish();
		}
	}

	public double getLeftDistanceInches()
	{
		return mLeftPositionInches;
	}

	public double getRightDistanceInches()
	{
		return mRightPositionInches;
	}

	public double getDistance()
	{
		return (getLeftDistanceInches() + getRightDistanceInches()) / 2;
	}

	public double getLeftVelocity()
	{
		return mLeftVelocity;
	}

	public double getRightVelocity()
	{
		return mRightVelocity;
	}

	public double getAngleDegrees()
	{
		return mAngleDegrees;
	}

	public Rotation2d getAngle()
	{
		return mAngle;
	}

	public void resetSensors()
	{
		mLeftPositionInches = 0;
		mRightPositionInches = 0;
		mAngle = Rotation2d.identity();
		
		mLeftMaster1.setSelectedSensorPosition(0.0);
		mRightMaster1.setSelectedSensorPosition(0.0);
		mNavX.reset();		
		mAngleDegrees = 0;

		
	}

	public void reloadGains()
	{
		mLeftMaster1.config_kP(0, Constants.kDriveVelocityKP);
		mLeftMaster1.config_kI(0, Constants.kDriveVelocityKI);
		mLeftMaster1.config_kD(0, Constants.kDriveVelocityKD);
		mLeftMaster1.config_kF(0, Constants.kDriveVelocityKF);

		mRightMaster1.config_kP(0, Constants.kDriveVelocityKP);
		mRightMaster1.config_kI(0, Constants.kDriveVelocityKI);
		mRightMaster1.config_kD(0, Constants.kDriveVelocityKD);
		mRightMaster1.config_kF(0, Constants.kDriveVelocityKF);
	}

	public enum driveState{
        OPEN,
        VELOCITY,       
        MOTION_MAGIC,
        POSITION
    }
}
