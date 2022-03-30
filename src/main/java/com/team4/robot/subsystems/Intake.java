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
	private final TalonSRX mRollerMotor, mArmMotorLeft, mArmMotorRight;
	private final Solenoid mLeftPiston, mRightPiston;

	// State of pistons
	private static boolean mLeftPos, mRightPos = false;

	public mState state = mState.IDLE;

	public enum mState {
		FORWARD,
		REVERSE,
		IDLE
	}

	

	public Intake() {
		mRollerMotor = TalonFactory.createDefaultTalonSRX(Constants.kRollerMotorID);
		mRollerMotor.changeMotionControlFramePeriod(100);
		mRollerMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		mRollerMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mRollerMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);
		mRollerMotor.setInverted(true);
		
		mArmMotorLeft = TalonFactory.createDefaultTalonSRX(Constants.kArmMotorLeftID);
		mArmMotorLeft.changeMotionControlFramePeriod(100);
		mArmMotorLeft.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		mArmMotorLeft.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mArmMotorLeft.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);
			
		mArmMotorRight = TalonFactory.createDefaultTalonSRX(Constants.kArmMotorRightID);
		mArmMotorRight.changeMotionControlFramePeriod(100);
		mArmMotorRight.setControlFramePeriod(ControlFrame.Control_3_General, 20);
		mArmMotorRight.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General, 20, Constants.kCANTimeoutMs);
		mArmMotorRight.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0, 100, Constants.kCANTimeoutMs);
		//mArmMotorRight.setInverted(true);

		
		
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
		mRollerMotor.set(ControlMode.PercentOutput, Constants.kIntakeForwardPower);
		mArmMotorLeft.set(ControlMode.PercentOutput, Constants.kIntakeForwardPower);
		mArmMotorRight.set(ControlMode.PercentOutput, Constants.kIntakeForwardPower);
	}

	private void motorsReverse() {
		mRollerMotor.set(ControlMode.PercentOutput, Constants.kIntakeReversePower);
		mArmMotorLeft.set(ControlMode.PercentOutput, Constants.kIntakeReversePower);
		mArmMotorRight.set(ControlMode.PercentOutput, Constants.kIntakeReversePower);
	}

	private void motorsOff(){
		mRollerMotor.set(ControlMode.PercentOutput, Constants.kIntakeOff);
		mArmMotorLeft.set(ControlMode.PercentOutput, Constants.kIntakeOff);
		mArmMotorRight.set(ControlMode.PercentOutput, Constants.kIntakeOff);
	}
}