package com.team4.robot;

import com.team254.lib.util.DriveSignal;
import com.team254.lib.util.SynchronousPIDF;
import com.team4.robot.subsystems.Conveyor;
import com.team4.robot.subsystems.Drive;
import com.team4.robot.subsystems.Shooter;
import com.team4.robot.subsystems.Shooter.ShooterControlState;

import edu.wpi.first.wpilibj.Timer;

public class AutonExecute {
    private double mStartTime= 0;

    private final double mShootEnd = 3;


    private Drive mDrive = Drive.getInstance();
    private Shooter mShooter = Shooter.getInstance();
    private Conveyor mConveyor = new Conveyor();


    private SynchronousPIDF mDriveController;

    public AutonExecute()
    {
        mDriveController = new SynchronousPIDF(Constants.kDriveKP, Constants.kDriveKI, Constants.kDriveKD);
    }

    public void start()
    {
        mStartTime = Timer.getFPGATimestamp();
        mDriveController.setSetpoint(60);
    }

    public void periodic()
    {
        if (Timer.getFPGATimestamp() - mStartTime <= mShootEnd)
        {
            mShooter.setControlState(ShooterControlState.VELOCITY);
            if(Timer.getFPGATimestamp() >= 1 || Timer.getFPGATimestamp() <= 4){ // change when rpm setpoint becomes a constant
                mConveyor.state = Conveyor.mState.FORWARD;
            }else{
                mConveyor.state = Conveyor.mState.IDLE;
            }
        }
        else
        {
            mConveyor.state = Conveyor.mState.IDLE;
            mShooter.setControlState(ShooterControlState.IDLE);

            double demand = 0/* mDriveController.calculate(mDrive.getDistance()) */;
            mDrive.setOpenLoop(new DriveSignal(demand, demand));       
        }
    }
}