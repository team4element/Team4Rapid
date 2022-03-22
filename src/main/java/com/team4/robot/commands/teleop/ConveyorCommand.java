package com.team4.robot.commands.teleop;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;
import com.team4.robot.controllers.OperatorController;
import com.team4.robot.controllers.TeleopControls;
import com.team4.robot.subsystems.Conveyor;

public class ConveyorCommand extends CommandBase {

    OperatorController mOperatorController = TeleopControls.mOperatorController;

    @Override
    public void start() {
        
    }

    @Override
    public void execute() {
        if(mOperatorController.conveyorForward()){
            Robot.mConveyor.state = Conveyor.mState.FORWARD;
          } else if (mOperatorController.conveyorBackward()){
            Robot.mConveyor.state = Conveyor.mState.REVERSE;
          }else{
            Robot.mConveyor.state = Conveyor.mState.IDLE;
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
