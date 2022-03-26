package com.team4.robot.commands;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.Intake;

public class SetIntakeCommand extends CommandBase {

    public SetIntakeCommand()
    {

    }

    @Override
    public void start() {
        Robot.mIntake.state = Intake.mState.FORWARD;
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void stop() {
        Robot.mIntake.state = Intake.mState.IDLE;
    }
    
}
