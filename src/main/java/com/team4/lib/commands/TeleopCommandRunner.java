package com.team4.lib.commands;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj2.command.RunCommand;

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
