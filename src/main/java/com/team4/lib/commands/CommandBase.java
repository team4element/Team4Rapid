package com.team4.lib.commands;

public abstract class CommandBase {
    
    public abstract void start();

    public abstract void periodic();

    public abstract boolean isFinished();

    public abstract void stop();
}
