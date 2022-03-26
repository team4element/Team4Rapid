package com.team4.robot.commands;

import com.team254.lib.control.Path;
import com.team254.lib.util.DriveSignal;
import com.team4.lib.commands.CommandBase;
import com.team4.lib.path.PathContainer;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.Drive;

import edu.wpi.first.wpilibj.Timer;

public class DrivePathCommand extends CommandBase{
    private PathContainer mPathContainer;
    private Path mPath;
    private Drive mDrive = Robot.mDrive;
    private boolean mStopWhenDone;

    public DrivePathCommand(PathContainer p, boolean stopWhenDone) {
        mPathContainer = p;
        mPath = mPathContainer.buildPath();
        mStopWhenDone = stopWhenDone;
    }

    public DrivePathCommand(PathContainer p) {
        this(p, true);
    }


    @Override
    public void start() {
        mDrive.setPath(mPath, mPathContainer.isReversed());    
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
        if(mStopWhenDone)
        {
            mDrive.setBrakeMode();
        }
        mDrive.setOpenLoop(DriveSignal.NEUTRAL);
    }


}
