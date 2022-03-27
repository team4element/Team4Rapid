package com.team4.robot.commands;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Constants;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.Conveyor;
import com.team4.robot.subsystems.Shooter;

public class ShootAndConveyCommand extends CommandBase {

    Conveyor mConveyor = Robot.mConveyor;
    Shooter mShooter = Robot.mShooter; 
    
    public ShootAndConveyCommand()
    {

    }

    @Override
    public void start() {
        mConveyor.state = Conveyor.mState.IDLE;
        mShooter.state = Shooter.mState.IDLE;
    }

    @Override
    public void execute() {
        mShooter.state = Shooter.mState.HIGH_VELOCITY;
        if(mShooter.getBottomVelocity() >= Constants.kHighVelocityBottom - 100){
                mConveyor.state = Conveyor.mState.FORWARD; 
        }else{
            mConveyor.state = Conveyor.mState.IDLE;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {
        mConveyor.state = Conveyor.mState.IDLE;
        mShooter.state = Shooter.mState.IDLE;
    }
}
