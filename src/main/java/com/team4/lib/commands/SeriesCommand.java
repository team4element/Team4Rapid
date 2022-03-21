package com.team4.lib.commands;

import java.util.List;

public class SeriesCommand extends CommandBase{
    private List<CommandBase> mCommands = null;

    public SeriesCommand(List<CommandBase> commands)
    {
        mCommands = commands;
    }

    @Override
    public void start() {}

    @Override
    public void execute() {

        for (CommandBase c : mCommands)
        {
            c.start();
            while(!c.isFinished())
            {
                c.execute();
            }
            c.stop();
        }

        mCommands.clear();
    }

    @Override
    public boolean isFinished() {
        return mCommands.isEmpty();
    }

    @Override
    public void stop() {}
}
