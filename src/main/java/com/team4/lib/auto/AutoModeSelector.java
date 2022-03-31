package com.team4.lib.auto;

import java.util.Arrays;
import java.util.List;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class AutoModeSelector {
    
    List<AutoBase> mAutoModes;

    private final SendableChooser<AutoBase> mChooser;

    public AutoModeSelector(AutoBase... modes)
    {
        mChooser = new SendableChooser<>();
        mAutoModes = Arrays.asList(modes);
        configSelector();
    }

    public void configSelector()
    {
        mChooser.setDefaultOption(mAutoModes.get(0).getName(), mAutoModes.get(0));
        for(int i = 1; i < mAutoModes.size(); i++)
        {
            mChooser.addOption(mAutoModes.get(i).getName(), mAutoModes.get(i));
        }

        SmartDashboard.putData("Auto Mode Selector", mChooser);
    }

    public AutoBase getAutoMode()
    {
        return mChooser.getSelected();
    }

}
