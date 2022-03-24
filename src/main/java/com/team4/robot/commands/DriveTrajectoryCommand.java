package com.team4.robot.commands;

import com.team254.lib.util.DriveSignal;
import com.team4.lib.commands.CommandBase;
import com.team4.lib.trajectory.SimpleTrajectory;
import com.team4.lib.util.FieldState;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Timer;

public class DriveTrajectoryCommand extends CommandBase{

    Drive mDrive = Robot.mDrive;
    FieldState mFieldState = Robot.mFieldState;

    private boolean mResetPose = false;
    
    private SimpleTrajectory mTrajectory;

    public DriveTrajectoryCommand(SimpleTrajectory trajectory, boolean resetPose)
    {
        mTrajectory = trajectory;
        mResetPose = resetPose;
    }

    public DriveTrajectoryCommand(SimpleTrajectory trajectory)
    {
        this(trajectory, false);
    }

    @Override
    public void start() {
        if (mResetPose)
        {
            mFieldState.reset(Timer.getFPGATimestamp(), mTrajectory.startPose());
        }
        
        mDrive.setPath(mTrajectory);
    }

    @Override
    public void execute() {
        mDrive.updatePathFollower(Timer.getFPGATimestamp());        
    }

    @Override
    public boolean isFinished() {
        return mDrive.isDoneWithTrajectory();
    }

    @Override
    public void stop() {
        mDrive.setOpenLoop(DriveSignal.NEUTRAL);        
    }
    
}
