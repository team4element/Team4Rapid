package com.team4.robot.controllers;

import com.team4.robot.Constants;
import com.team4.robot.controllers.XboxController.Button;

public class DriverController {
    private final XboxController mController;

    public DriverController() {
        mController = new XboxController(Constants.kDriverControlSlot);
    }

    public double getThrottle() {
        return -1 * mController.getJoystick(XboxController.Side.LEFT, XboxController.Axis.Y);
    }

    public double getTurn() {
        return mController.getJoystick(XboxController.Side.RIGHT, XboxController.Axis.X);
    }

		public boolean getIsDeployIntake() {
			return mController.getButtonClick(Button.A);
		}

		public boolean getIsCompressorToggle() {
			return mController.getButtonClick(Button.LB);
		}
}
