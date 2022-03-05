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

    private final double kDrivePower = 0.25;
    private final double mShootEnd = 3;
    private final double mDriveEnd = 5;
    
    private boolean isFirstTime = true; 

    private Drive mDrive = Robot.mDrive;
    private Shooter mShooter = Shooter.getInstance();
    private Conveyor mConveyor = Robot.mConveyor;

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
            if(isFirstTime)
            {
                System.out.println("Finished shooting");
                start();
                isFirstTime = false;
            }



            mConveyor.state = Conveyor.mState.IDLE;
            mShooter.setControlState(ShooterControlState.IDLE);
            
            if (Timer.getFPGATimestamp() - mStartTime <= mDriveEnd)
            {
                
                System.out.println("Running drive");
                double demand = kDrivePower/* mDriveController.calculate(mDrive.getDistance()) */;
                mDrive.setOpenLoop(new DriveSignal(demand, demand));       
            }
            else
            {
                System.out.println("Finished drive");
                mDrive.setOpenLoop(new DriveSignal(0d, 0d));                       
            }

        }
    }
}
