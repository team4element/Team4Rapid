package com.team4.robot.automodes;

import com.team4.lib.auto.AutoBase;
import com.team4.robot.commands.SetDriveCommand;

public class TuneDriveMode extends AutoBase {

    @Override
    public void routine() {
        runCommand(new SetDriveCommand(120));        
    }
    
}
