package com.team4.robot.controllers;

import com.team4.robot.controllers.XboxController.Button;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;

public class OperatorController {
    private final XboxController mController;

    public OperatorController() {
        // TODO: Add Constant
        mController = new XboxController(1);
    }

    public boolean getVision()
    {
        return mController.getButton(Button.RB);
    }
}
