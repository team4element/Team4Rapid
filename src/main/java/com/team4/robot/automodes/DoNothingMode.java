package com.team4.robot.automodes;

import com.team4.lib.auto.AutoBase;
import com.team4.lib.commands.WaitCommand;

public class DoNothingMode extends AutoBase{

    @Override
    public String getName() {
        return "Do Nothing Mode";
    }

    @Override
    public void routine() {
        runCommand(new WaitCommand(15));
    }
    
}
