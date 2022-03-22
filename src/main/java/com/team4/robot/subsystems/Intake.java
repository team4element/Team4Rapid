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
	// Hardware
	private final TalonSRX mRollerMotor, mArmMotor;
	private final Solenoid mLeftPiston, mRightPiston;

	// State of pistons
	private static boolean mLeftPos, mRightPos = false;


	public enum mState {
		FORWARD,
		REVERSE,
		IDLE
	}

	public mState state = mState.IDLE;

	public Intake() {
		mRollerMotor = TalonFactory.createDefaultTalonSRX(Constants.kRollerMotorID);
		mRollerMotor.changeMotionControlFramePeriod(100);
		mRollerMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		mRollerMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mRollerMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);
		mRollerMotor.setInverted(true);
		
		mArmMotor = TalonFactory.createDefaultTalonSRX(Constants.kArmMotorID);
		mArmMotor.changeMotionControlFramePeriod(100);
		mArmMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		mArmMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mArmMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);
		
		
		mLeftPiston = new Solenoid(1, PneumaticsModuleType.CTREPCM, Constants.kIntakeSolenoidLeft);
		mRightPiston = new Solenoid(1, PneumaticsModuleType.CTREPCM, Constants.kIntakeSolenoidRight);
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
		mLeftPiston.set(mLeftPos);
		mRightPiston.set(mRightPos);
	}

	@Override
	public synchronized void readPeriodicInputs() {

	}

	@Override
	public synchronized void writePeriodicOutputs() {
		mLeftPos = mLeftPiston.get();
		mRightPos = mRightPiston.get();
	}

	public void moveArm()
	{
		mLeftPiston.toggle();;
		mRightPiston.toggle();
	}

	private void motorsForward() {
		mRollerMotor.set(ControlMode.PercentOutput, Constants.kDriveEnconderPPR);
		mArmMotor.set(ControlMode.PercentOutput, Constants.kIntakeForwardPower);
	}

	private void motorsReverse() {
		mRollerMotor.set(ControlMode.PercentOutput, Constants.kIntakeReversePower);
		mArmMotor.set(ControlMode.PercentOutput, Constants.kIntakeReversePower);
	}

	private void motorsOff(){
		mRollerMotor.set(ControlMode.PercentOutput, Constants.kIntakeOff);
		mArmMotor.set(ControlMode.PercentOutput, Constants.kIntakeOff);
	}
}