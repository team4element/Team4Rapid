package com.team4.robot.automodes;

import com.team4.lib.auto.AutoBase;
import com.team4.robot.commands.DriveTrajectoryCommand;
import com.team4.robot.paths.TestStraightPath;

public class TestTrajectoryMode extends AutoBase {

    @Override
    public void routine() {
        runCommand(new DriveTrajectoryCommand(new TestStraightPath()));
    }
    
}
