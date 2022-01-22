package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.robot.Constants;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

class IntakePeriodicIO implements Loggable {
	@Log
	public double demand;
	@Log
	public boolean isDeployed;
}

public class Intake extends Subsystem<IntakePeriodicIO> {
	private static Intake mInstance = null;
	private IntakePeriodicIO mPeriodicIO;
	private IntakeState mCurrentState = IntakeState.IDLE;

	private final WPI_TalonFX mIntakeMotor1;
	private final Solenoid mLeftPiston;
	private final Solenoid mRightPiston;
	
	public static enum IntakeState {
		FWD,
		REVERSE,
		IDLE,
	}

	private Intake() {
		mPeriodicIO = new IntakePeriodicIO();

		mIntakeMotor1 = TalonFactory.createDefaultTalonFX(Constants.kIntakeMaster1);
		mIntakeMotor1.setInverted(true);

		// TODO: Document what these parameters mean
		mIntakeMotor1.changeMotionControlFramePeriod(100);
		mIntakeMotor1.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		// TODO: Don't I need to add logging if failed?
		mIntakeMotor1.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mIntakeMotor1.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);


		// TODO: Matthew: Is our PCM on the default module?
		mLeftPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.kIntakeSolenoidLeft);
		mRightPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.kIntakeSolenoidRight);
	}

	public void setControlState(IntakeState newState) {
		mCurrentState = newState;
	}

	public void setIntakeSpeed(double demand) {
		mPeriodicIO.demand = demand;
	}

	// TODO: Which do we want?
	public void setDeployed(boolean isDeployed) {
		mPeriodicIO.isDeployed = isDeployed;
	}

	public void toggleDeploy() {
		mPeriodicIO.isDeployed = !mPeriodicIO.isDeployed;
	}

	@Override
	public void onLoop(double timestamp) {
		// TODO: Should we have 3 motor states * 2 deploy states to handle all states?
		switch(mCurrentState) {
			case IDLE:
			setIntakeSpeed(0);
			break;
			case FWD:
			// TODO: I can make this speed tunable from smart dashboard!
				setIntakeSpeed(0.4);
				break;
			case REVERSE:
				setIntakeSpeed(-0.4);
				break;
			default:
				break;
		}
	}

	@Override
	public void onDisableLoop() {
		// Reset intake state to initial values
		// mPeriodicIO = new IntakePeriodicIO();
		writePeriodicOutputs();
	}

	@Override
	// TODO: Add intake sensors. Look at 254 for sensors that we can use.
	// Maybe I can monitor current draw if a jam occurs? In that case, I can auto reject?
	// Maybe I can monitor current draw. If there is a spike, I have intaken a ball!
	// Maybe I can maintain a counter when I notice this?
	// Or Maybe I have a break sensor to know when a ball has entered the robot!
	public synchronized void readPeriodicInputs() {

	}

	@Override
	public synchronized void writePeriodicOutputs() {
		SmartDashboard.putNumber("Test111", mPeriodicIO.demand);
		mIntakeMotor1.set(ControlMode.PercentOutput, mPeriodicIO.demand);

		// TODO: I can create an abstraction around the set which debounces.
		if (mPeriodicIO.isDeployed != mLeftPiston.get()) {
			mLeftPiston.set(mPeriodicIO.isDeployed);
			mRightPiston.set(mPeriodicIO.isDeployed);
		}
	}

	public static Intake getInstance() {
		if (mInstance == null) {
			mInstance = new Intake();
		}

		return mInstance;
	}
}
