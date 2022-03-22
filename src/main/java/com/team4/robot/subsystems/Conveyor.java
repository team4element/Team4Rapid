package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.robot.Constants;



public class Conveyor extends Subsystem {
	private static final Conveyor mInstance = new Conveyor();

	// Hardware
	private final WPI_TalonFX mConveyorLeft;
	private final WPI_TalonFX mConveyorRight;

	 
	// Performance Settings
	private static final double kConveyorForwardPower = 0.3;
	// Ava comment: changed from 0.7 to 0.3  so the ball would stop bouncing between the flywheels
	private static final double kConveyorReversePower = -0.35;
	private static final double kConveyorOff = 0;

	public enum mState {
		FORWARD,
		REVERSE,
		IDLE
	}

	public mState state = mState.IDLE;

	public static Conveyor getInstance()
	{
		return mInstance;
	}

	public Conveyor() {

		mConveyorLeft = new WPI_TalonFX(Constants.kConveyorLeft);
		mConveyorRight = new WPI_TalonFX(Constants.kConveyorRight);
		mConveyorRight.setInverted(true);



		// mConveyor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		

	}

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
		// if(state == mState.FORWARD)
		// {
		// 	System.out.println("Current demand " + kConveyorForwardPower);
		// }
		// else if(state == mState.REVERSE)
		// {
		// 	System.out.println("Current demand " + kConveyorReversePower);
		// }
		// else if(state == mState.IDLE)
		// {
		// 	System.out.println("Current demand " + kConveyorOff);
		// }
				
	}

	private void motorOff(){
		mConveyorLeft.set(ControlMode.PercentOutput, kConveyorOff);
		mConveyorRight.set(ControlMode.PercentOutput, kConveyorOff);

	}

	private void motorForward(){
		mConveyorLeft.set(ControlMode.PercentOutput, kConveyorForwardPower);
		mConveyorRight.set(ControlMode.PercentOutput, kConveyorForwardPower);

	}

	private void motorReverse(){
		mConveyorLeft.set(ControlMode.PercentOutput, kConveyorReversePower);
		mConveyorRight.set(ControlMode.PercentOutput, kConveyorReversePower);

	} 

}
