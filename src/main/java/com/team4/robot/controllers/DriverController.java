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

	//CHANGE IN CODE: -1 *
    public double getTurn() {
        return  -1 * mController.getRightX();
    }

		public boolean getDeployIntake() {
			return mController.getRightBumper();
		}

		public boolean getClimbUp()
		{
			return mController.getPOV() == 90;
		}

		public boolean getClimbDown()
		{
			return mController.getPOV() == 270;
		}
		
}
