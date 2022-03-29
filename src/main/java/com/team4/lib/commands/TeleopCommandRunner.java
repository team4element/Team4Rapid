package com.team4.lib.commands;

public abstract class TeleopCommandRunner  {

    public abstract void runTeleop();    

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
