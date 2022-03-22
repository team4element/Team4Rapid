package com.team4.robot.commands.teleop;

import com.team254.lib.util.DriveSignal;
import com.team4.lib.commands.CommandBase;
import com.team4.lib.util.DriveHelper;
import com.team4.robot.Robot;
import com.team4.robot.controllers.DriverController;
import com.team4.robot.controllers.TeleopControls;

public class DriveCommand extends CommandBase{

    DriverController mDriverController = TeleopControls.mDriverController;
    DriveHelper mDriveHelper = DriveHelper.getInstance();


    @Override
    public void start() {
    }

    @Override
    public void execute() {
        Robot.mDrive.setOpenLoop(mDriveHelper.elementDrive(mDriverController.getThrottle(), mDriverController.getTurn(), false));
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
