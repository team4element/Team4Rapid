package com.team4.lib.commands;

import edu.wpi.first.wpilibj.Timer;

public class WaitCommand extends CommandBase{
    private double mDuration, mStartTime;

    public WaitCommand(double duration)
    {
        mDuration = duration;
    }


    @Override
    public void start() {
        mStartTime = Timer.getFPGATimestamp();
    }

    @Override
    public void execute() {}

    @Override
    public boolean isFinished() {
        return (Timer.getFPGATimestamp() - mStartTime > mDuration);
    }

    @Override
    public void stop() {}

}
