package com.team4.robot.commands.teleop;

import com.team4.lib.commands.OneTimeCommand;
import com.team4.robot.Robot;

public class ToggleWinchCommand extends OneTimeCommand {
    @Override
    public void runOnce() {
        Robot.mClimber.toggleWinch();
    }
}
