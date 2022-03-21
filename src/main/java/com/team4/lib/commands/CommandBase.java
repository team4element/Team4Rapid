package com.team4.lib.commands;

public abstract class CommandBase {
    
    public abstract void start();

    public abstract void execute();

    public abstract boolean isFinished();

    public abstract void stop();
}
