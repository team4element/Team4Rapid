package com.team4.robot.subsystems;
import com.ctre.phoenix.motorcontrol.ControlFrame;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.team4.lib.drivers.LazyTalonFX;
import com.team4.lib.drivers.TalonUtil;
import com.team4.lib.util.ElementMath;
import com.team4.robot.Constants;

public class Shooter extends Subsystem {
	private final LazyTalonFX mTopMotor, mBottomMotor;
	private double mBottomVelocity = 0d;
	private double mTopVelocity = 0d;
	public mState state = mState.IDLE;

	public Shooter() {
		//Bottom Motor
		mBottomMotor = new LazyTalonFX(Constants.kShooterBottom);
			configureEncoder(mBottomMotor);
		mBottomMotor.changeMotionControlFramePeriod(100);
        mBottomMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
        mBottomMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General,
			10, Constants.kCANTimeoutMs);
        mBottomMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0,
			100, Constants.kCANTimeoutMs);
		

		//Top Motor
        mTopMotor = new LazyTalonFX(Constants.kShooterTop);
		configureEncoder(mTopMotor);
        mTopMotor.changeMotionControlFramePeriod(100);
        mTopMotor.setControlFramePeriod(ControlFrame.Control_3_General, 20);
        mTopMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General,
                10, Constants.kCANTimeoutMs);
		mTopMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0,
                100, Constants.kCANTimeoutMs);
		mTopMotor.setInverted(true);
        
		reloadGains();
	}

	@Override
	public void onLoop(double timestamp) {
		synchronized(this){
			switch (state){
				case HIGH_VELOCITY:
					setVelocity(Constants.kHighVelocityBottom, Constants.kHighVelocityTop);
					break;
				case LOW_VELOCITY:
					setVelocity(Constants.kLowVelocityBottom, Constants.kHighVelocityTop);
					break;
				case IDLE:
					stopMotors();
				default:
					break;
			}
		}
	}

	@Override
	public void onDisableLoop() {
		mBottomMotor.set(TalonFXControlMode.PercentOutput, 0);
        mTopMotor.set(TalonFXControlMode.PercentOutput, 0);    
	}

	@Override
	public void readPeriodicInputs() {
        mBottomVelocity = ElementMath.tickPer100msToScaledRPM(mBottomMotor.getSelectedSensorVelocity(0),
			Constants.kShooterEnconderPPR, Constants.kShooterBottomGearRatio);
		mTopVelocity = ElementMath.tickPer100msToScaledRPM(mTopMotor.getSelectedSensorVelocity(0),
			Constants.kShooterEnconderPPR, Constants.kShooterTopGearRatio);
		System.out.println("Bottom velocity " + mBottomVelocity + "Top velocity: " + mTopVelocity);
	}

	@Override
	public void writePeriodicOutputs() {
	}

	@Override
	public void onSimulationLoop() {
	}

	public enum mState {
		HIGH_VELOCITY,
		LOW_VELOCITY,
		IDLE
	}


	public void setVelocity(double bottomVelocity, double topVelocity){
        configureVelocityTalon();
		mBottomMotor.set(ControlMode.Velocity, 
			ElementMath.scaleRPM(ElementMath.rpmToTicksPer100ms(bottomVelocity, Constants.kShooterEnconderPPR),
								Constants.kShooterBottomGearRatio));
		mTopMotor.set(ControlMode.Velocity, 
			ElementMath.scaleRPM(ElementMath.rpmToTicksPer100ms(topVelocity, Constants.kShooterEnconderPPR),
			Constants.kShooterTopGearRatio));
			
    }

	private void stopMotors(){
        mBottomMotor.set(ControlMode.PercentOutput, 0);
        mTopMotor.set(ControlMode.PercentOutput, 0);
    }

	public void reloadGains(){
        mBottomMotor.config_kP(0, Constants.kShooterBottomKp);
        mBottomMotor.config_kI(0, Constants.kShooterBottomKi);
        mBottomMotor.config_kD(0, Constants.kShooterBottomKd);
        mBottomMotor.config_kF(0, Constants.kShooterBottomKf);

        mTopMotor.config_kP(0, Constants.kShooterTopKp);
        mTopMotor.config_kI(0, Constants.kShooterTopKi);
        mTopMotor.config_kD(0, Constants.kShooterTopKd);
        mTopMotor.config_kF(0, Constants.kShooterTopKf);
    }

    public synchronized void resetEncoders() {
        mBottomMotor.setSelectedSensorPosition(0, 0, 0);
        mTopMotor.setSelectedSensorPosition(0, 0, 0);
    }

	private void configureVelocityTalon(){
        mBottomMotor.selectProfileSlot(0, 0);
		mBottomMotor.configClosedloopRamp(0);
		mTopMotor.selectProfileSlot(0, 0);
		mTopMotor.configClosedloopRamp(0);
    }

	public static void configureEncoder(LazyTalonFX talon) {
		TalonUtil.checkError(talon.setStatusFramePeriod(
			StatusFrame.Status_2_Feedback0, 20, Constants.kLongCANTimeoutMs),
			"could not set drive feedback frame");

		TalonUtil.checkError(
				talon.configSelectedFeedbackSensor(
					TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.kLongCANTimeoutMs),
					"could not detect motor encoder"); 

		talon.setSensorPhase(true);
	}

	public double getBottomVelocity(){
		return mBottomVelocity;
	}
}
