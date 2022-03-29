package com.team4.robot.controllers;

import com.team4.robot.Constants;

import edu.wpi.first.wpilibj.XboxController;


public class OperatorController {
    private double triggerTolerance = .25;
    private final XboxController mController;

    public OperatorController() {
        mController = new XboxController(Constants.kOperatorControlSlot);
    }

    public boolean intakeForward() {
        return mController.getLeftBumper();
    }

    public boolean intakeBackwards(){
        return mController.getLeftTriggerAxis() > 0.25;
    }

    public boolean shooterHigh(){
        return mController.getAButton();
    }

    public boolean shooterLow(){
        return mController.getYButton();
    }

    public boolean conveyorForward(){
        return mController.getRightBumper();
    }

    public boolean conveyorBackward(){
        return mController.getRightTriggerAxis() > triggerTolerance;
    }

    public boolean getConveyorReverse(){
        return mController.getRightTriggerAxis() > triggerTolerance;
    }

    public boolean getCompressorToggle(){
        return mController.getBButton();
    }

    public boolean getDeployIntake(){
        return mController.getXButtonPressed();
    }

    public boolean changeSecondaryClimb(){
        return mController.getAButtonPressed();
    }
}
