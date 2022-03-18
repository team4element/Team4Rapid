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

	/*
		public boolean getDeployIntake() {
			return mController.getRightBumperPressed();
		}
	*/

		public boolean getClimbUp()
		{
			return mController.getPOV() == 0;
		}

		public boolean getClimbDown()
		{
			return mController.getPOV() == 180;
		}

		public boolean changeWinch(){
			return mController.getYButtonPressed();
		}
		
}
