package com.team4.robot.commands.teleop;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;
import com.team4.robot.controllers.OperatorController;
import com.team4.robot.controllers.TeleopControls;
import com.team4.robot.subsystems.Shooter;

public class ShooterCommand extends CommandBase {

    OperatorController mOperatorController = TeleopControls.mOperatorController;

    @Override
    public void start() {
        
    }

    @Override
    public void execute() {
        if (mOperatorController.shooterHigh()) {
            // Robot.mShooter.state = Shooter.mState.HIGH_VELOCITY;
            Robot.mDrive.resetSensors();
        } else if (mOperatorController.shooterLow()) {
            // Robot.mShooter.state = Shooter.mState.LOW_VELOCITY;	
        } else {
            Robot.mShooter.state = Shooter.mState.IDLE;
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {
        
    }
    
}
