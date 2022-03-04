package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.robot.Constants;



public class Conveyor extends Subsystem {

	// Hardware
	private final WPI_TalonFX mConveyor;
	 
	// Performance Settings
	private static final double kConveyorForwardPower = 0.5;
	private static final double kConveyorReversePower = -0.35;
	private static final double kConveyorOff = 0;

	public enum mState {
		FORWARD,
		REVERSE,
		IDLE
	}

	public mState state = mState.IDLE;

	public Conveyor() {

		mConveyor = new WPI_TalonFX(Constants.kConveyor);

		// mConveyor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		// mConveyor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);

	}

	/**
	 * If my mWantedState is:
	 * INTAKE: If it is within kIntakeStowTime, then I'm going to IDLE.
	 * EXHAUST: Return Exhaust
	 * IDLE: If it is within the timeframe where I last stowed, I actually want to LIGHT_INTAKE
	 * DEFAULT: IDLE
	 */
	@Override
	public void onLoop(double timestamp) {
		// now we have to act upon this new system state.

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
		mConveyor.set(ControlMode.PercentOutput, kConveyorOff);
	}

	private void motorForward(){
		mConveyor.set(ControlMode.PercentOutput, kConveyorForwardPower);
	}

	private void motorReverse(){
		mConveyor.set(ControlMode.PercentOutput, kConveyorReversePower);
	} 

}
