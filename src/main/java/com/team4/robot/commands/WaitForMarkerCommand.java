package com.team4.robot.commands;

import com.team4.lib.commands.CommandBase;
import com.team4.robot.Robot;

public class WaitForMarkerCommand extends CommandBase {

    String mMarker;

    public WaitForMarkerCommand(String marker)
    {
        mMarker = marker;
    }

    @Override
    public void start() { }

    @Override
    public void execute() { }

    @Override
    public boolean isFinished() {
        return Robot.mDrive.hasPassedMarker(mMarker);
    }

    @Override
    public void stop() { }
    
}
