package com.team4.robot.controllers;

import com.team4.robot.Constants;

public class OperatorController {
    private final XboxController mController;

    private OperatorController() {
        // TODO: Add Constant
        mController = new XboxController(Constants.kOperatorControlSlot);
    }
}
