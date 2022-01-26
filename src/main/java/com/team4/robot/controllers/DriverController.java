package com.team4.robot.controllers;

import com.team4.robot.Constants;
import com.team4.robot.controllers.XboxController.Axis;
import com.team4.robot.controllers.XboxController.Side;

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

		public boolean getDeployIntake() {
			return mController.getButton(XboxController.Button.B);
		}

		public boolean getRetractIntake() {
			return mController.getButton(XboxController.Button.Y);
		}

		// TODO: Add 254 Function Key for button mapping overlays?
		public boolean getIsCompressorToggle() {
			return mController.getButtonClick(XboxController.Button.X);
		}

		public boolean getExhaust() {
			return mController.getButton(XboxController.Button.LB);
		}

		public boolean getIntake() {
			return mController.getButton(XboxController.Button.RB);
		}

		public boolean getShooterRPM() {
			return mController.getButton(XboxController.Button.X);
		}
}
