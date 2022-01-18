package com.team4.robot.controllers;

import com.team4.robot.Constants;

public class DriverController {
    private final XboxController mController;

    public DriverController() {
        // TODO: Add Constant
        mController = new XboxController(Constants.kDriverControlSlot);
    }

    public double getThrottle() {
        return -1 * mController.getJoystick(XboxController.Side.LEFT, XboxController.Axis.Y);
    }

    public double getTurn() {
        return mController.getJoystick(XboxController.Side.RIGHT, XboxController.Axis.X);
    }
}
