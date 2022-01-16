package com.team4.controllers;

public class DriverController {
    private final XboxController mController;

    public DriverController() {
        // TODO: Add Constant
        mController = new XboxController(0);
    }

    public double getThrottle() {
        return -1 * mController.getJoystick(XboxController.Side.LEFT, XboxController.Axis.Y);
    }

    public double getTurn() {
        return mController.getJoystick(XboxController.Side.RIGHT, XboxController.Axis.X);
    }
}
