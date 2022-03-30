package com.team4.robot.automodes;

import java.util.Arrays;

import com.team4.lib.auto.AutoBase;
import com.team4.lib.commands.ParallelCommand;
import com.team4.lib.commands.SeriesCommand;
import com.team4.lib.commands.TimedCommand;
import com.team4.lib.commands.WaitCommand;
import com.team4.robot.commands.DrivePathCommand;
import com.team4.robot.commands.MoveArmCommand;
import com.team4.robot.commands.ReverseConveyorCommand;
import com.team4.robot.commands.SetIntakeCommand;
import com.team4.robot.commands.ShootAndConveyCommand;
import com.team4.robot.commands.WaitForMarkerCommand;
import com.team4.robot.paths.threeball.ReturnPath;
import com.team4.robot.paths.threeball.TwoBallPath;

public class ThreeBallShootAndDriveMode extends AutoBase{

    @Override
    public void routine() {
        runCommand(new MoveArmCommand());
        runCommand(new TimedCommand(new ShootAndConveyCommand(), 1.75));
        runCommand(new MoveArmCommand());
        runCommand(new ParallelCommand(Arrays.asList(
            new DrivePathCommand(new TwoBallPath()),
            new SeriesCommand(Arrays.asList(
                new WaitForMarkerCommand("Intake One"),
                new MoveArmCommand(),
                new TimedCommand(new SetIntakeCommand(), 0.4)
            )),
            new SeriesCommand(Arrays.asList(
                new WaitForMarkerCommand("Intake One"),
                new WaitCommand(2),
                new TimedCommand(new SetIntakeCommand(), 1.2)
            ))
        )));
        runCommand(new TimedCommand(new ReverseConveyorCommand(), 0.25));
        runCommand(new DrivePathCommand(new ReturnPath()));
        runCommand(new TimedCommand(new ShootAndConveyCommand(), 2.0));
    }    
}
