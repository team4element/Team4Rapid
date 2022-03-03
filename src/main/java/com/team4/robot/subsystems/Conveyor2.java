package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.robot.Constants;
import com.team4.robot.subsystems.Conveyor.SystemState;

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



	public void setWantedState(SystemState systemState) {
		mSystemState = systemState;
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
