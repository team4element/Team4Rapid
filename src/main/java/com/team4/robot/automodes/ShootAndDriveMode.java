package com.team4.robot.automodes;

import com.team4.lib.auto.AutoBase;
import com.team4.lib.commands.TimedCommand;
import com.team4.lib.commands.WaitCommand;
import com.team4.robot.commands.MoveArmCommand;
import com.team4.robot.commands.SetDriveCommand;
import com.team4.robot.commands.ShootAndConveyCommand;

public class ShootAndDriveMode extends AutoBase {

    @Override
    public String getName() {
        return "One Ball Auto Mode";
    }

    @Override
    public void routine() {
        runCommand(new MoveArmCommand());
        runCommand(new TimedCommand(new ShootAndConveyCommand(), 2));
        runCommand(new TimedCommand(new SetDriveCommand(10 * 12), 5));        
        runCommand(new WaitCommand(15));
    }
}
