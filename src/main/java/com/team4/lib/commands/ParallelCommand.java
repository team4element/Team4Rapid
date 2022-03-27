package com.team4.lib.commands;

import java.util.List;

public class ParallelCommand extends CommandBase{
    
    private List<CommandBase> mCommands = null;

    public ParallelCommand(List<CommandBase> commands)
    {
        mCommands = commands;
    }

    @Override
    public void start() {
        mCommands.forEach(c -> c.start());
    }

    @Override
    public void execute() {
        mCommands.forEach(c -> c.execute());
    }

    @Override
    public boolean isFinished() {
        for(CommandBase c : mCommands)
        {
            if(!c.isFinished())
            {
                return false;
            }
            else
            {
                c.stop();
            }
        }
        return true;
    }

    @Override
    public void stop() {
        mCommands.forEach(c -> c.stop());
    }



}
