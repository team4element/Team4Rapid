package com.team4.robot.automodes;

import com.team4.lib.auto.AutoBase;
import com.team4.robot.commands.DrivePathCommand;
import com.team4.robot.commands.MoveArmCommand;
import com.team4.robot.paths.threeball.ReturnPath;
import com.team4.robot.paths.threeball.TwoBallPath;

public class TestDrivePathMode extends AutoBase{

    @Override
    public void routine() {
        runCommand(new MoveArmCommand());
        runCommand(new DrivePathCommand(new TwoBallPath()));
        runCommand(new DrivePathCommand(new ReturnPath()));
    }
    
}
