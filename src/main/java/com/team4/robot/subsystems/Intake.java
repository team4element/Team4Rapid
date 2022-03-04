package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.robot.Constants;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;


public class Intake extends Subsystem {
	//CHANGE IN CODE
	// Hardware
	private final TalonSRX mRollerMotor;
	private final TalonSRX mArmMotor;
	private final Solenoid mLeftPiston;
	private final Solenoid mRightPiston;

	// Performance Settings
	private static final double kIntakeForwardPower = 0.75;
	private static final double kIntakeReversePower = -0.75;
	private static final double kIntakeOff = 0d;
	// private static final double kLightIntakePower = 0.1;

	public enum mState {
		FORWARD,
		REVERSE,
		IDLE
	}

	public mState state = mState.IDLE;

	public Intake() {


		mRollerMotor = TalonFactory.createDefaultTalonSRX(Constants.kRollerMotorID);
		mArmMotor = TalonFactory.createDefaultTalonSRX(Constants.kArmMotorID);
		mRollerMotor.setInverted(true);
		// mArmMotor.follow(mRollerMotor);
		//mArmMotor.setInverted(true);

		// TODO: Document what these parameters mean
		mRollerMotor.changeMotionControlFramePeriod(100);
		mRollerMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		mArmMotor.changeMotionControlFramePeriod(100);
		mArmMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		// TODO: Don't I need to add logging if failed?
		mRollerMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mRollerMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);
		mArmMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mArmMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);

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
		if(mLeftPiston.get())
		{
			System.out.println("Left Piston Present with id" + mLeftPiston.getChannel());
		}
		else if (mRightPiston.get())
		{
			System.out.println("Right Piston Present with id " + mRightPiston.getChannel());
		}
	}

	@Override
	public synchronized void writePeriodicOutputs() {
	}

	public void moveArm()
	{
		mLeftPiston.toggle();;
		mRightPiston.toggle();
	}

	private void motorsForward() {
		mRollerMotor.set(ControlMode.PercentOutput, kIntakeForwardPower);
		mArmMotor.set(ControlMode.PercentOutput, kIntakeForwardPower);
	}

	private void motorsReverse() {
		mRollerMotor.set(ControlMode.PercentOutput, kIntakeReversePower);
		mArmMotor.set(ControlMode.PercentOutput, kIntakeReversePower);
	}

	private void motorsOff(){
		mRollerMotor.set(ControlMode.PercentOutput, kIntakeOff);
		mArmMotor.set(ControlMode.PercentOutput, kIntakeOff);
	}
}