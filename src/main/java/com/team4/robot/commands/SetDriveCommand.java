package com.team4.robot.commands;

import com.team254.lib.util.DriveSignal;
import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;

public class SetDriveCommand extends CommandBase{

    private double mSpeed;

    public SetDriveCommand(double speed)
    {
        mSpeed = speed;
    }

    @Override
    public void start() {
    }

    @Override
    public void execute() {
        Robot.mDrive.setOpenLoop(new DriveSignal(mSpeed, mSpeed));
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {
        Robot.mDrive.setOpenLoop(new DriveSignal(0d, 0d));
    }
    
}
