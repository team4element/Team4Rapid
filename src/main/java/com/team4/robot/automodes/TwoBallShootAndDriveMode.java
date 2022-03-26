package com.team4.robot.automodes;

import java.util.Arrays;

import com.team4.lib.auto.AutoBase;
import com.team4.lib.commands.ParallelCommand;
import com.team4.lib.commands.SeriesCommand;
import com.team4.lib.commands.TimedCommand;
import com.team4.lib.commands.WaitCommand;
import com.team4.robot.commands.MoveArmCommand;
import com.team4.robot.commands.SetDriveCommand;
import com.team4.robot.commands.SetIntakeCommand;
import com.team4.robot.commands.ShootAndConveyCommand;

public class TwoBallShootAndDriveMode extends AutoBase {

    @Override
    public void routine() {
        runCommand(new MoveArmCommand());
        runCommand(new TimedCommand(new ShootAndConveyCommand(), 3));
        runCommand(new ParallelCommand(Arrays.asList(
            new TimedCommand(new SetDriveCommand(10.0 * 12.0), 4),
            new SeriesCommand(Arrays.asList(
                new WaitCommand(2),
                new TimedCommand(new SetIntakeCommand(), 3) 
            )))));
        runCommand(new TimedCommand(new SetDriveCommand(-10.0 * 12.0), 4));
        runCommand(new TimedCommand(new ShootAndConveyCommand(), 3));
        runCommand(new WaitCommand(15.0));
    }
}
