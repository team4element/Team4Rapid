package com.team4.robot.controllers;

import com.team4.robot.Constants;

import edu.wpi.first.wpilibj.XboxController;

public class DriverController {
    private final XboxController mController;

    public DriverController() {
        mController = new XboxController(Constants.kDriverControlSlot);
    }

    public double getThrottle() {
        return -1 * mController.getLeftY();
    }

    public double getTurn() {
        return mController.getRightX();
    }

		public boolean getDeployIntake() {
			return mController.getXButton();
		}

		public boolean getIsCompressorToggle() {
			return mController.getStartButton();
		}

		public boolean getExhaust() {
			return mController.getLeftBumper();
		}

		public boolean getIntake() {
			return mController.getRightBumper();
		}

		public boolean getIsShooterOn() {
			return mController.getYButton();
		}
}
