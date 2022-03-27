package com.team4.robot.commands;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.Conveyor;

public class ReverseConveyorCommand extends CommandBase {

    @Override
    public void start() {
        Robot.mConveyor.state = Conveyor.mState.REVERSE;
    }

    @Override
    public void execute() {
        
    }

    @Override
    public boolean isFinished() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void stop() {
        Robot.mConveyor.state = Conveyor.mState.IDLE;
    }
    
}
