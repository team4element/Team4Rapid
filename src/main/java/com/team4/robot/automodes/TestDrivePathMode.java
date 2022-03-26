package com.team4.robot.automodes;

import com.team4.lib.auto.AutoBase;
import com.team4.robot.commands.DrivePathCommand;
import com.team4.robot.paths.TestStraightPath;

public class TestDrivePathMode extends AutoBase{

    @Override
    public void routine() {
        runCommand(new DrivePathCommand(new TestStraightPath()));
    }
    
}
