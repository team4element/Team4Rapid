package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.robot.Constants;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

/*
class IntakePeriodicIO implements Loggable {
	@Log
	public double demand;
	@Log
	public boolean isStowed = true;
}
*/

public class Conveyor2 extends Subsystem<ConveyorPeriodicIO> {
	// Internal State
	private static Conveyor2 mInstance = null;
	private ConveyorPeriodicIO mPeriodicIO;
	WantedState mWantedState = WantedState.IDLE;
	SystemState mSystemState = SystemState.IDLE;

	// Hardware
	private final WPI_TalonFX mConveyor;
	 
	// Performance Settings
	private static final double kIntakePower = 0.75;
	private static final double kIntakeExhaustPower = -0.25;
	private static final double kLightIntakePower = 0.1;

	private static final double kIntakeStowTime = 0.5; // seconds, time it takes to stow intake.
	private static final double kLightIntakeTime = 1.0; // seconds. Time to run intake after stowing


	// They run the intake for 1 second AFTER the intake was stowed at a light speed. For ball jamming issues prob.
	private double mLastStowTime = 0.0;

	
	public static enum WantedState {
		IDLE,
		INTAKE,
		EXHAUST, FEED,
	}

	public static enum SystemState {
		IDLE,
		INTAKING,
		EXHAUSTING,
		LIGHT_INTAKE,
	}

	private Conveyor2() {
		mPeriodicIO = new ConveyorPeriodicIO();


		mConveyor = TalonFactory.createDefaultTalonFX(Constants.kConveyor);

		// TODO: Document what these parameters mean
		mConveyor.changeMotionControlFramePeriod(100);
		mConveyor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		// TODO: Don't I need to add logging if failed?
		mConveyor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mConveyor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);

	}

	public void deploy() {
		mLastStowTime = 0.0;
		mPeriodicIO.isStowed = false;
	}

	public void stow() {
		if (!mPeriodicIO.isStowed) {
			mLastStowTime = Timer.getFPGATimestamp();
		}
		mPeriodicIO.isStowed = true;
		mPeriodicIO.demand = 0.0;
	}

	public void setWantedState(WantedState wantedState) {
		mWantedState = wantedState;
	}

	/**
	 * This method transforms the mWantedState into a mSystemState.
	 * It overrides the mWantedState in two ways:
	 * 1. A user cannot INTAKE and STOW simultaneously.
	 * 2. The Intake will run at LIGHT_INTAKE after the intake is STOWED for a short duration.
	 * 
	 * @param timestamp
	 * @return
	 */
	private SystemState getSystemStateFromWantedState(double timestamp) {
		double timeSinceStowCommandIssued = timestamp - mLastStowTime; // Last time we stowed the intake
		double timeAfterStowCompleted = timeSinceStowCommandIssued - kIntakeStowTime; // Time after stow finished
		boolean isStowInProgress = timeAfterStowCompleted > 0;

		switch (mWantedState) {
			case INTAKE:
			/*
				 if (isStowInProgress) {
				 	return SystemState.IDLE;
				 }
			*/
				return SystemState.INTAKING;
			case EXHAUST:
				return SystemState.EXHAUSTING;
			case IDLE:
				if (!isStowInProgress && timeAfterStowCompleted < kLightIntakeTime) {
					return SystemState.LIGHT_INTAKE;
				}
				return SystemState.IDLE;
			default:
				return SystemState.IDLE;
		}
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
		SmartDashboard.putString("WantedState", mWantedState.toString());
		SmartDashboard.putString("SystemState", mSystemState.toString());
		// Gets the current state of the robot. TODO??
		SystemState newState = getSystemStateFromWantedState(timestamp);

		if (newState != mSystemState) {
			System.out.println("Intake Changing State!" + mSystemState + " -> " + newState);
			mSystemState = newState;
		}

		// now we have to act upon this new system state.

		switch (mSystemState) {
			case IDLE:
				mPeriodicIO.demand = 0.0;
				break;
			case INTAKING:
				mPeriodicIO.demand = kIntakePower;
				break;
			case EXHAUSTING:
				mPeriodicIO.demand = kIntakeExhaustPower;
				break;
			case LIGHT_INTAKE:
				mPeriodicIO.demand = kLightIntakePower;
				break;
			default:
				System.out.println("Unexpected Intake System State: " + mSystemState);
				break;
		}
	}

	@Override
	public void onDisableLoop() {
		// Reset intake state to initial values
		writePeriodicOutputs();
	}

	@Override
	// Maybe I can monitor current draw if a jam occurs? In that case, I can auto reject?
	// Maybe I can monitor current draw. If there is a spike, I have intaken a ball!
	// Maybe I can maintain a counter when I notice this?
	// Or Maybe I have a break sensor to know when a ball has entered the robot!
	public synchronized void readPeriodicInputs() {

	}

	@Override
	public synchronized void writePeriodicOutputs() {
		mConveyor.set(ControlMode.PercentOutput, mPeriodicIO.demand);

	}

	public static Conveyor2 getInstance() {
		if (mInstance == null) {
			mInstance = new Conveyor2();
		}

		return mInstance;
	}
}
