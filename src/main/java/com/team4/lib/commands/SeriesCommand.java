package com.team4.lib.commands;

import java.util.ArrayList;
import java.util.List;

public class SeriesCommand extends CommandBase{
    private CommandBase mCurrentCommand;
    private final ArrayList<CommandBase> mRemainingCommands;
    
    public SeriesCommand(List<CommandBase> commands)
    {
        mRemainingCommands = new ArrayList<>(commands);
        mCurrentCommand = null;
    }

    @Override
    public void start() {}

    @Override
    public void execute() {

        if (mCurrentCommand == null) {
            if (mRemainingCommands.isEmpty()) {
                return;
            }

            mCurrentCommand = mRemainingCommands.remove(0);
            mCurrentCommand.start();
        }

        mCurrentCommand.execute();

        if (mCurrentCommand.isFinished()) {
            mCurrentCommand.stop();
            mCurrentCommand = null;
        }
    }

    @Override
    public boolean isFinished() {
        return mRemainingCommands.isEmpty() && mCurrentCommand == null;
    }

    @Override
    public void stop() {}
}
