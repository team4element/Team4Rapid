package com.team4.robot.subsystems;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team254.lib.geometry.Twist2d;
import com.team4.lib.util.FieldState;
import com.team4.lib.util.Kinematics;
import com.team4.robot.Robot;

import edu.wpi.first.wpilibj.Timer;

public class StateEstimator extends Subsystem {

    FieldState mFieldState = Robot.mFieldState;
    Drive mDrive = Robot.mDrive;

    private double mLeftPrevDistance = 0.0;
    private double mRightPrevDistance = 0.0;
    private double mPrevTimestamp = -1.0;
    private Rotation2d mPrevAngle = null ;
    

    @Override
    public void readPeriodicInputs() {
        System.out.println("Robot X: " + 
                mFieldState.getFieldToVehicle(Timer.getFPGATimestamp()).getTranslation().x()
                );
     }

    @Override
    public void writePeriodicOutputs() { }

    @Override
    public void onLoop(double timestamp) 
    {

        double leftDistance = mDrive.getLeftDistanceInches();
        double rightDistance = mDrive.getRightDistanceInches();
        Rotation2d angle = mDrive.getAngle();

        double dt = timestamp - mPrevTimestamp;
        double dLeft = leftDistance - mLeftPrevDistance;
        double dRight = rightDistance - mRightPrevDistance;
        Twist2d odometry;
        
        synchronized (mFieldState) {
            Pose2d last_measurement = mFieldState.getLatestFieldToVehicle().getValue();
            odometry = Kinematics.forwardKinematics(last_measurement.getRotation(), dLeft,
                    dRight, angle);
        } 

        Twist2d measuredVel = Kinematics.forwardKinematics(
                    dLeft, 
                    dRight, 
                    mPrevAngle.inverse().rotateBy(angle).getRadians()).scaled(1.0 / dt);
               
               
        Twist2d predictedVel = Kinematics.forwardKinematics(
                    mDrive.getLeftVelocity(),
                    mDrive.getRightVelocity()
                    ).scaled(dt);

            mFieldState.addObservations(timestamp, odometry, measuredVel,
                predictedVel);
            
            mLeftPrevDistance = leftDistance;
            mRightPrevDistance = rightDistance;
            mPrevAngle = angle;
            mPrevTimestamp = timestamp;
    }

    @Override
    public void onEnabledStart()
    {
        mLeftPrevDistance = mDrive.getLeftDistanceInches();
        mRightPrevDistance = mDrive.getRightDistanceInches();
        mPrevTimestamp = Timer.getFPGATimestamp();
        mPrevAngle = mDrive.getAngle();
    }

    @Override
    public void onDisableLoop() { }
    
}
