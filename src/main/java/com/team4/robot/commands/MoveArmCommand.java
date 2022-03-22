package com.team4.robot.commands;

import com.team4.lib.commands.OneTimeCommand;
import com.team4.robot.Robot;

public class MoveArmCommand extends OneTimeCommand {

    @Override
    public void runOnce() {
        Robot.mIntake.moveArm();
    }

    
    
}
