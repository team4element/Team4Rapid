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
        runCommand(new TimedCommand(new ShootAndConveyCommand(), 2.0));
        runCommand(new ParallelCommand(Arrays.asList(
            new TimedCommand(new SetDriveCommand(86.0), 1.0),
            new SeriesCommand(Arrays.asList(
                new WaitCommand(0.5),
                new TimedCommand(new SetIntakeCommand(), 1.25) 
            )))));
        runCommand(new TimedCommand(new SetDriveCommand(0.0), 1.0));
        runCommand(new TimedCommand(new ShootAndConveyCommand(), 2.0));
    }
}
