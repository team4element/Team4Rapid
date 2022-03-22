package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.lib.drivers.LazyTalonFX;
import com.team4.robot.Constants;



public class Conveyor extends Subsystem {
	private final WPI_TalonFX mConveyorLeft;
	private final WPI_TalonFX mConveyorRight;

	public mState state = mState.IDLE;

	public Conveyor() {
		mConveyorLeft = new LazyTalonFX(Constants.kConveyorLeft);
		mConveyorRight = new LazyTalonFX(Constants.kConveyorRight);
		mConveyorRight.setInverted(true);
	}

	@Override
	public void onLoop(double timestamp) {
		switch (state) {
			case IDLE:
				motorOff();
				break;
			case FORWARD:
				motorForward();
				break;
			case REVERSE:
				motorReverse();
				break;
			default:
				state = mState.IDLE;
				break;
		}
	}

	@Override
	public void onDisableLoop() {
		state = mState.IDLE;
	}

	@Override
	public synchronized void readPeriodicInputs() {
	}

	@Override
	public synchronized void writePeriodicOutputs() {
	}

	private void motorOff(){
		mConveyorLeft.set(ControlMode.PercentOutput, Constants.kConveyorOff);
		mConveyorRight.set(ControlMode.PercentOutput, Constants.kConveyorOff);
	}

	private void motorForward(){
		mConveyorLeft.set(ControlMode.PercentOutput, Constants.kConveyorForwardPower);
		mConveyorRight.set(ControlMode.PercentOutput, Constants.kConveyorForwardPower);
	}

	private void motorReverse(){
		mConveyorLeft.set(ControlMode.PercentOutput, Constants.kConveyorReversePower);
		mConveyorRight.set(ControlMode.PercentOutput, Constants.kConveyorReversePower);
	}

	public enum mState {
		FORWARD,
		REVERSE,
		IDLE
	}
}
