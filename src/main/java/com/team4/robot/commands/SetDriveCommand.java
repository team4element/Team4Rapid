package com.team4.robot.commands;

import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.SynchronousPIDF;
import com.team4.lib.commands.CommandBase;
import com.team4.lib.util.DriveHelper;
import com.team4.robot.Constants;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.Drive;

public class SetDriveCommand extends CommandBase{

    SynchronousPIDF mDistancePIDF, mAnglePIDF;
    Drive mDrive = Robot.mDrive;

    private double mDistance;

    public SetDriveCommand(double distance)
    {
        mDistance = distance;
        mDistancePIDF = new SynchronousPIDF(Constants.kDriveDistanceKP, Constants.kDriveDistanceKI, Constants.kDriveDistanceKD);
        mAnglePIDF = new SynchronousPIDF(Constants.kDriveAngleKP, Constants.kDriveAngleKI, Constants.kDriveAngleKD);
    }

    @Override
    public void start() {
        mDistancePIDF.setSetpoint(mDistance);
        mAnglePIDF.setSetpoint(Robot.mDrive.getAngleDegrees());
    }

    @Override
    public void execute() {
        mDrive.setOpenLoop(
          DriveHelper.elementDrive(mDistancePIDF.calculate(mDrive.getDistance()), 
                                    mAnglePIDF.calculate(mDrive.getAngleDegrees()), false) 
        );
    
    }

    @Override
    public boolean isFinished() {
        return mDistancePIDF.onTarget(Constants.kDistancePidTolerance);
    }

    @Override
    public void stop() {
        Robot.mDrive.setOpenLoop(new DriveSignal(0d, 0d));
    }
}
