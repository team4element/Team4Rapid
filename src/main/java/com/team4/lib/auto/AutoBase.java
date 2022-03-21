package com.team4.lib.auto;

import com.team4.lib.commands.CommandBase;

public abstract class AutoBase {
    public abstract void routine();

    public void done()
    {
        System.out.println("Auto mode finished");
    }

    public void runCommand(CommandBase command)
    {
        command.start();

        while(!command.isFinished())
        {
            command.execute();
        }

        command.stop();
    }
}
