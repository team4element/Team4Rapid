package com.team4.lib.commands;

public abstract class OneTimeCommand extends CommandBase{

    @Override
    public void start() {
        runOnce();
    }

    @Override
    public void execute() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public boolean isFinished() {
        return true;
    }

    @Override
    public void stop() {
        
    }
    
    public abstract void runOnce();
    

}
