package com.team4.robot.commands;

import com.team254.lib.util.DriveSignal;
import com.team4.lib.commands.CommandBase;
import com.team4.lib.util.ElementMath;
import com.team4.robot.Constants;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.Drive;

public class DriveVelocity extends CommandBase {

    Drive mDrive = Robot.mDrive;

    double mSetpoint;

    public DriveVelocity(double setpoint)
    {
        mSetpoint = ElementMath.inchesToRotations(
                        ElementMath.rpmToTicksPer100ms(setpoint * 60, Constants.kDriveEnconderPPR), 
                        Constants.kDriveWheelCircumferenceInches, 
                        Constants.kDriveGearRatio);
    }

    @Override
    public void start() {
        mDrive.resetSensors();
    }

    @Override
    public void execute() {
        mDrive.setVelocity(new DriveSignal(mSetpoint, mSetpoint), DriveSignal.NEUTRAL);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {
        mDrive.setOpenLoop(DriveSignal.NEUTRAL);        
    }
    
}
