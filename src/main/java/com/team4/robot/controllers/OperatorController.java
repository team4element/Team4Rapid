package com.team4.robot.controllers;

import com.team4.robot.Constants;

import edu.wpi.first.wpilibj.XboxController;

public class OperatorController {
    private final XboxController mController;

    public OperatorController() {
        // TODO: Add Constant
        mController = new XboxController(Constants.kOperatorControlSlot);
    }

    public boolean getIntake() {
        return mController.getLeftBumper();
    }

    public boolean getExhaust() {
        return mController.getLeftTriggerAxis() > 0.25;
    }

    public boolean getIsShooterOn() {
        return mController.getAButton();
    }

    public boolean getConveyor() {
        return mController.getRightBumper();
    }

    public boolean getConveyorReverse()
    {
        return mController.getRightTriggerAxis() > 0.25;
    }

    public boolean getCompressorToggle()
    {
        return mController.getRightStickButtonPressed();
    }
}
