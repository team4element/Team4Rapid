package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.lib.drivers.LazyTalonFX;
import com.team4.lib.drivers.TalonUtil;
import com.team4.lib.util.ElementMath;
import com.team4.robot.Constants;
import com.team4.robot.controllers.OperatorController;

import edu.wpi.first.wpilibj.GenericHID.RumbleType;


public class Shooter extends Subsystem {

	public double kHighVelocity = 3200;
	// Changed from 3100 to 3500 to ensure a sufficient height is always reached
	public final double kLowVelocity = 1500; // Find me
	private final LazyTalonFX mMasterMotor, mSlaveMotor;

	public mState state = mState.OPEN_LOOP;

	private double mVelocity = 0d;
	private double mTopVelocity = 0d;

	public static void configureEncoder(WPI_TalonFX talon, boolean left, boolean main_encoder_talon) {
		talon.setInverted(!left);

		if (main_encoder_talon) {

			TalonUtil.checkError(talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 20, Constants.kLongCANTimeoutMs),
					"could not set drive feedback frame");

			TalonUtil.checkError(
					talon.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.kLongCANTimeoutMs),
					"could not detect motor encoder"); 

			talon.setSensorPhase(true);
		}
	}


	public Shooter() {
		mMasterMotor = new LazyTalonFX(Constants.kShooterMaster1);
		
		configureEncoder(mMasterMotor, true, true);
		
		mMasterMotor.changeMotionControlFramePeriod(100);
        mMasterMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
        mMasterMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General,
		10, Constants.kCANTimeoutMs);
        mMasterMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0,
		100, Constants.kCANTimeoutMs);

        mSlaveMotor = new LazyTalonFX(Constants.kShooterFollower2);
		configureEncoder(mMasterMotor, false, false);
        mSlaveMotor.changeMotionControlFramePeriod(100);
        mSlaveMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
        mSlaveMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General,
                10, Constants.kCANTimeoutMs);
        mSlaveMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0,
                100, Constants.kCANTimeoutMs);
				
		// mMasterMotor.setInverted(false); //bot motor
		mSlaveMotor.setInverted(true); // top motor


        setBrakeMode(false);
		
		reloadGains();
	}

	public enum mState {
		OPEN_LOOP,
		HIGH_VELOCITY,
		LOW_VELOCITY,
		IDLE
	}

	// Used to override shooter speed
	public void setOpenLoop(double pow){
        mMasterMotor.set(ControlMode.PercentOutput, pow);
        mSlaveMotor.set(ControlMode.PercentOutput, pow);
    }

	public synchronized void setBrakeMode(boolean on) {
            NeutralMode mode = on ? NeutralMode.Brake : NeutralMode.Coast;
            mMasterMotor.setNeutralMode(mode);
            mSlaveMotor.setNeutralMode(mode);
	}

	@Override
	public void onLoop(double timestamp) {
		synchronized(this){
			switch (state){
				case OPEN_LOOP:
					setOpenLoop(1);
					break;
				case HIGH_VELOCITY:
					setVelocity(2200, -(2500));
					break;
				case LOW_VELOCITY:
					setVelocity(kLowVelocity, -(1200));
					break;
				case IDLE:
					setOpenLoop(0);
				default:
					break;
			}
		}
	}

	@Override
	public void onDisableLoop() {
		mMasterMotor.set(TalonFXControlMode.PercentOutput, 0);
        mSlaveMotor.set(TalonFXControlMode.PercentOutput, 0);    
	}

	@Override
	public void readPeriodicInputs() {
        mVelocity = ElementMath.tickPer100msToScaledRPM(mMasterMotor.getSelectedSensorVelocity(0), Constants.kShooterEnconderPPR, Constants.kShooterGearRatio);
		mTopVelocity = ElementMath.tickPer100msToScaledRPM(mSlaveMotor.getSelectedSensorVelocity(0), Constants.kShooterEnconderPPR, Constants.kShooterGearRatio);
		//if (mVelocity == -2800){
		//	OperatorController.setRumble(RumbleType.kRightRumble, 1);
		//}
		 System.out.println("Shooter Velocity bot: " + mVelocity + "__________" + "Shooter Velocity top: " + mTopVelocity);
		 //System.out.println("Shooter Velocity top: " + mTopVelocity);
	}

	@Override
	public void writePeriodicOutputs() {
	}

	public void setVelocity(double frontVelocity, double backVelocity){
        if(state != mState.HIGH_VELOCITY || state != mState.LOW_VELOCITY){
            configureVelocityTalon();
            state = mState.HIGH_VELOCITY;
        }

		mMasterMotor.set(ControlMode.Velocity, ElementMath.rpmToTicksPer100ms(backVelocity, Constants.kShooterEnconderPPR));
		mSlaveMotor.set(ControlMode.Velocity, ElementMath.rpmToTicksPer100ms(frontVelocity, Constants.kShooterEnconderPPR));
    }

	@Override
	public void onSimulationLoop() {
	}

	public void reloadGains(){
        mMasterMotor.config_kP(0, Constants.kShooterKp);
        mMasterMotor.config_kI(0, Constants.kShooterKi);
        mMasterMotor.config_kD(0, Constants.kShooterKd);
        mMasterMotor.config_kF(0, Constants.kShooterKf);

        mSlaveMotor.config_kP(0, Constants.kShooterKp);
        mSlaveMotor.config_kI(0, Constants.kShooterKi);
        mSlaveMotor.config_kD(0, Constants.kShooterKd);
        mSlaveMotor.config_kF(0, Constants.kShooterKf);
    }

	public void zeroSensors() {
        resetEncoders();
    }

    public synchronized void resetEncoders() {
        mMasterMotor.setSelectedSensorPosition(0, 0, 0);
        mSlaveMotor.setSelectedSensorPosition(0, 0, 0);
    }

	public double getVelocity()
	{
		return mVelocity;
	}

	private void configureVelocityTalon(){

        // mMasterMotor.setNeutralMode(NeutralMode.Brake);
        mMasterMotor.selectProfileSlot(0, 0);
		mSlaveMotor.selectProfileSlot(0, 0);

        mMasterMotor.configClosedloopRamp(0);
		mSlaveMotor.configClosedloopRamp(0);
    

        System.out.println("Switching shooter to velocity");
    }
}
