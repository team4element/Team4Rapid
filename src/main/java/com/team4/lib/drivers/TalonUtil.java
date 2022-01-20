// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team4.lib.drivers;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import edu.wpi.first.wpilibj.DriverStation;

/** Add your docs here. */
public class TalonUtil {

    private static final int kLongCANTimeoutMs = 100;

    public static void configureTalonFX(TalonFX talon, boolean left, boolean main_encoder_talon) {
        // general
        talon.setInverted(!left);

        if (main_encoder_talon) {
            // status frames (maybe set for characterization?)
            checkError(talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, kLongCANTimeoutMs), "could not set drive feedback frame");
            checkError(talon.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, kLongCANTimeoutMs), "could not detect motor encoder"); //primary closed-loop, 100 ms timeout
            talon.setSensorPhase(true);
        }
        
        // implement if desire current limit
        // checkError(talon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 50, .5), kLongCANTimeoutMs), "Could not set supply current limits");
        // checkError(talon.configStatorCurrentLimit(new StatorCurrentLimitConfiguration(true, 60, 60, 0.2), kLongCANTimeoutMs), "Could not set stator current limits");

        // implement if desire voltage compensation
        // checkError(talon.configVoltageCompSaturation(12.0, kLongCANTimeoutMs), "could not config voltage comp saturation");
        // talon.enableVoltageCompensation(true);
    }

    public static void configureTalonSRX(TalonSRX talon, boolean left, boolean main_encoder_talon) {
        // general
        talon.setInverted(!left);

        if (main_encoder_talon) {
            // status frames (maybe set for characterization?)
            checkError(talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, kLongCANTimeoutMs), "could not set drive feedback frame");
            checkError(talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, kLongCANTimeoutMs), "could not detect motor encoder"); //primary closed-loop, 100 ms timeout
            talon.setSensorPhase(true);
        }

        // implement if desired current limit
        // checkError(talon.configSupplyCurrentLimit(new SupplyCurrentLimitConfiguration(true, 40, 50, .5), kLongCANTimeoutMs), "Could not set supply current limits");
        
        // implement if desire voltage compensation
        // checkError(talon.configVoltageCompSaturation(12.0, kLongCANTimeoutMs), "could not config voltage comp saturation");
        // talon.enableVoltageCompensation(true);
    }


    public static void checkError(ErrorCode errorCode, String message) {
        if (errorCode != ErrorCode.OK) {
            DriverStation.reportError(message + errorCode, false);
        }
    }

}
