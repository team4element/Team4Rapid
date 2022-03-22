package com.team4.robot.commands.teleop;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;
import com.team4.robot.controllers.OperatorController;
import com.team4.robot.controllers.TeleopControls;
import com.team4.robot.subsystems.Intake;

public class IntakeCommand extends CommandBase {

    OperatorController mOperatorController = TeleopControls.mOperatorController;

    @Override
    public void start() {
        
    }

    @Override
    public void execute() {
        if (mOperatorController.intakeForward()) {
			Robot.mIntake.state = Intake.mState.FORWARD;
		} else if (mOperatorController.intakeBackwards()) {
            Robot.mIntake.state = Intake.mState.REVERSE;
		} else {
            Robot.mIntake.state = Intake.mState.IDLE;
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
