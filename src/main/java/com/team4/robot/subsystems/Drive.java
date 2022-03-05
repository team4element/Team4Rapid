// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team254.lib.util.DriveSignal;
import com.team4.lib.drivers.TalonFactory;
import com.team4.lib.drivers.TalonUtil;
import com.team4.lib.util.ElementMath;
import com.team4.robot.Constants;
import com.team4.robot.subsystems.states.TalonControlState;


public class Drive extends Subsystem {

	// Motors that controls wheels
	private final WPI_TalonFX mLeftMaster1, mleftFollower2;
	private final WPI_TalonFX mRightMaster1, mRightFollower2;

	private TalonControlState mTalonControlState;

	double mLeftPositionInches = 0d;
	double mRightPositionInches = 0d;
	
	public static void configureTalonFX(WPI_TalonFX talon, boolean left, boolean main_encoder_talon) {
		talon.setInverted(!left);

		if (main_encoder_talon) {

			TalonUtil.checkError(talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, Constants.kLongCANTimeoutMs),
					"could not set drive feedback frame");

			TalonUtil.checkError(
					talon.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.kLongCANTimeoutMs),
					"could not detect motor encoder"); // primary closed-loop, 100 ms timeout

			talon.setSensorPhase(true);
		}
	}

	/** Creates a new Drive. */
	public Drive() {
		// Starts all Talons in Coast Mode
		mLeftMaster1 = TalonFactory.createDefaultTalonFX(Constants.kDriveLeftMaster1);
		configureTalonFX(mLeftMaster1, true, true);

		mleftFollower2 = TalonFactory.createPermanentSlaveTalonFX(Constants.kDriveLeftFollower2, mLeftMaster1);
		configureTalonFX(mleftFollower2, true, false);

		mRightMaster1 = TalonFactory.createDefaultTalonFX(Constants.kDriveRightMaster1);
		configureTalonFX(mRightMaster1, false, true);

		mRightFollower2 = TalonFactory.createPermanentSlaveTalonFX(Constants.kDriveRightFollower2, mRightMaster1);
		configureTalonFX(mRightFollower2, false, false);

		setCoastMode();
	}

	public synchronized void setCoastMode() {
			mLeftMaster1.setNeutralMode(NeutralMode.Coast);
			mleftFollower2.setNeutralMode(NeutralMode.Coast);
			mRightMaster1.setNeutralMode(NeutralMode.Coast);
			mRightFollower2.setNeutralMode(NeutralMode.Coast);
	}

	private void configureOpenTalon() {
		setCoastMode();

		mLeftMaster1.configOpenloopRamp(0.25);
		mRightMaster1.configOpenloopRamp(0.25);
		mTalonControlState = TalonControlState.OPEN;
	}

	public synchronized void setOpenLoop(DriveSignal signal) {
		if (mTalonControlState != TalonControlState.OPEN) {
			configureOpenTalon();
		}

		mLeftMaster1.set(ControlMode.PercentOutput, signal.getLeft());
		mRightMaster1.set(ControlMode.PercentOutput, signal.getRight());
	}

	@Override
	public void readPeriodicInputs() {
		// While enabled, gets information from sensors
		mLeftPositionInches = ElementMath.rotationsToInches(ElementMath.ticksToRotations(mLeftMaster1.getSelectedSensorPosition(), 
																	Constants.kDriveEnconderPPR), 
																	Constants.kDriveWheelCircumferenceInches, 
																	Constants.kDriveGearRatio);
		
		mRightPositionInches = ElementMath.rotationsToInches(ElementMath.ticksToRotations(mRightMaster1.getSelectedSensorPosition(), 
																	Constants.kDriveEnconderPPR), 
																	Constants.kDriveWheelCircumferenceInches,
																	Constants.kDriveGearRatio);

	}

	@Override
	public synchronized void writePeriodicOutputs() {
	}

	@Override
	public void onDisableLoop() {
		setCoastMode();
	}

	@Override
	public void onLoop(double timestamp) {
	}

	public double getLeftDistanceInches()
	{
		return mLeftPositionInches;
	}

	public double getRightDistanceInches()
	{
		return mRightPositionInches;
	}

	public double getDistance()
	{
		return (getLeftDistanceInches() + getRightDistanceInches()) / 2;
	}

}
