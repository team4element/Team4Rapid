package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.robot.Constants;
import com.team4.robot.controllers.OperatorController;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;


public class Intake extends Subsystem {
	// Internal State
	private static Intake mInstance = null;


	//CHANGE IN CODE
	// Hardware
	private final TalonSRX mIntakeMotor1;
	private final TalonSRX mIntakeMotor2;
	private final Solenoid mLeftPiston;
	private final Solenoid mRightPiston;

	// Performance Settings
	private static final double kIntakeForwardPower = 0.75;
	private static final double kIntakeReversePower = -0.25;
	// private static final double kLightIntakePower = 0.1;

	enum mState {
		FORWARD,
		REVERSE,
		IDLE
	}

	public mState state = IDLE;


	private Intake() {
		mPeriodicIO = new IntakePeriodicIO();


		mIntakeMotor1 = TalonFactory.createDefaultTalonSRX(Constants.kIntakeMaster1);
		mIntakeMotor2 = TalonFactory.createDefaultTalonSRX(Constants.kIntakeLast);
		mIntakeMotor1.setInverted(true);
		mIntakeMotor2.follow(mIntakeMotor1);
		//mIntakeMotor2.setInverted(true);

		// TODO: Document what these parameters mean
		mIntakeMotor1.changeMotionControlFramePeriod(100);
		mIntakeMotor1.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		mIntakeMotor2.changeMotionControlFramePeriod(100);
		mIntakeMotor2.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		// TODO: Don't I need to add logging if failed?
		mIntakeMotor1.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mIntakeMotor1.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);
		mIntakeMotor2.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mIntakeMotor2.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);

		// TODO: Matthew: Is our PCM on the default module?
		mLeftPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.kIntakeSolenoidLeft);
		mRightPiston = new Solenoid(PneumaticsModuleType.CTREPCM, Constants.kIntakeSolenoidRight);
	}

	@Override
	public void onLoop(double timestamp) {
		switch (state) {
			case IDLE:
				motorsOff();
				break;
			case FORWARD:
				motorsForward();
				break;
			case REVERSE:
				motorsReverse();
				break;
			default:
				state = IDLE;
				break;
		}
	}

	@Override
	public void onDisableLoop() {
		state = IDLE;
		
	}

	@Override
	public synchronized void readPeriodicInputs() {

	}

	@Override
	public synchronized void writePeriodicOutputs() {
		mIntakeMotor1.set(ControlMode.PercentOutput, mPeriodicIO.demand);
		mIntakeMotor2.set(ControlMode.PercentOutput, mPeriodicIO.demand);

		mLeftPiston.set(mPeriodicIO.isStowed);
		mRightPiston.set(mPeriodicIO.isStowed);
	}

	private void motorsForward() {
		mIntakeMotor1.set(kIntakeForwardPower);
	}

	private void motorsReverse() {
		mIntakeMotor1.set(kIntakeReversePower);
	}

	private void motorsOff(){
		mIntakeMotor1.set(0);

	}
}