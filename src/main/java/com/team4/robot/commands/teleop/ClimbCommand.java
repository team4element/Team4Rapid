package com.team4.robot.commands.teleop;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;
import com.team4.robot.controllers.DriverController;
import com.team4.robot.controllers.TeleopControls;
import com.team4.robot.subsystems.Climber.ClimberControlState;

public class ClimbCommand extends CommandBase {

    DriverController mDriverController = TeleopControls.mDriverController;

    @Override
    public void start() {
        
    }

    @Override
    public void execute() {
        if (mDriverController.getClimbUp())
        {
            Robot.mClimber.setClimb(ClimberControlState.CLIMB_UP);
        }
        else if (mDriverController.getClimbDown())
        {
            Robot.mClimber.setClimb(ClimberControlState.CLIMB_DOWN);
        }
        else
        {
            Robot.mClimber.setClimb(ClimberControlState.IDLE);
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
