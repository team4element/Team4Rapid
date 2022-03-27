package com.team4.lib.commands;

import edu.wpi.first.wpilibj.Timer;

public class TimedCommand extends CommandBase {
    private CommandBase mCommand = null;
    private double mDuration, mStartTime;

    public TimedCommand(CommandBase command, double duration)
    {
        mCommand = command;
        mDuration = duration;        
    }


    @Override
    public void start() {
        mStartTime = Timer.getFPGATimestamp();
        mCommand.start();
    }

    @Override
    public void execute() {
        mCommand.execute();
    }

    @Override
    public boolean isFinished() {
        return mCommand.isFinished() || (Timer.getFPGATimestamp() - mStartTime >= mDuration);
    }

    @Override
    public void stop() {
        mCommand.stop();
    }
    
}
