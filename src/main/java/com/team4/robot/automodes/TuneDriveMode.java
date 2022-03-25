package com.team4.robot.automodes;

import com.team4.lib.auto.AutoBase;
import com.team4.robot.commands.DriveVelocity;

public class TuneDriveMode extends AutoBase {

    @Override
    public void routine() {
        runCommand(new DriveVelocity(5 * 12));        
    }
    
}
