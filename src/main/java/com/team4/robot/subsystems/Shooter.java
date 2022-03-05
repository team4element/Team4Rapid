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

import edu.wpi.first.wpilibj.Timer;
import io.github.oblarg.oblog.Loggable;

/**
 * RPM
 * RPM Target
 * RPM Delta
 * At RPM TARGET
 * SET RPM TARGET
 * SET MOTOR PERCENT
 * FLYWHEEL PERCENT
 * 
 * 
 */


class ShooterPeriodicIO implements Loggable {
	// Inputs
	// These are in Motor Units
	public double timestamp;
        public double demand;
        public double feedforward;
        public double position_ticks;
        public double accel;
        public double velocity;
}

public class Shooter extends Subsystem {
	private static Shooter mInstance = null;
	private ShooterPeriodicIO mPeriodicIO;
	public double kHighVelocity = 2900;
	private final LazyTalonFX mMasterMotor, mSlaveMotor;

	private ShooterControlState mControlState = ShooterControlState.OPEN_LOOP;


	public static void configureTalonFX(WPI_TalonFX talon, boolean left, boolean main_encoder_talon) {
		talon.setInverted(!left);
		// general
		// This is a manual override to enable drivetrain simulation to work.
		// Simulation will be very useful for debugging auton modes.

		if (main_encoder_talon) {
			// Talons are configured to perform different motor functions at different
			// frequencies.
			// These can be changed by calling setStatusFramePeriod().
			// Status1 (10ms): Motor Output Refresh
			// Status 2 (20ms): Sensor Updates
			// Status 3 (>100ms): Quadrature Information
			// Status 4 (>100ms): Analog Input/Supply Battery Voltage
			// Full Documentation can be found here
			// https://docs.ctre-phoenix.com/en/stable/ch18_CommonAPI.html#setting-status-frame-periods

			TalonUtil.checkError(talon.setStatusFramePeriod(StatusFrame.Status_2_Feedback0, 10, Constants.kLongCANTimeoutMs),
					"could not set drive feedback frame");

			// The Talon Motor Controller can have different feedback sensors.
			// Integrated Sensor: built in encoder
			// Other options are found here.
			// https://docs.ctre-phoenix.com/en/stable/ch14_MCSensor.html?highlight=configSelectedFeedbackSensor#sensor-options
			TalonUtil.checkError(
					talon.configSelectedFeedbackSensor(TalonFXFeedbackDevice.IntegratedSensor, 0, Constants.kLongCANTimeoutMs),
					"could not detect motor encoder"); // primary closed-loop, 100 ms timeout

			// Used to ensure that positive sensor readings correspond to positive
			// PercentOutput
			talon.setSensorPhase(true);
		}

		// implement if desire current limit
		// checkError(talon.configSupplyCurrentLimit(new
		// SupplyCurrentLimitConfiguration(true, 40, 50, .5), kLongCANTimeoutMs), "Could
		// not set supply current limits");
		// checkError(talon.configStatorCurrentLimit(new
		// StatorCurrentLimitConfiguration(true, 60, 60, 0.2), kLongCANTimeoutMs),
		// "Could not set stator current limits");

		// implement if desire voltage compensation
		// checkError(talon.configVoltageCompSaturation(12.0, kLongCANTimeoutMs), "could
		// not config voltage comp saturation");
		// talon.enableVoltageCompensation(true);
	}


	private Shooter() {
		mPeriodicIO = new ShooterPeriodicIO();
		
		mMasterMotor = new LazyTalonFX(Constants.kShooterMaster1);
		
		configureTalonFX(mMasterMotor, true, true);
		
		mMasterMotor.changeMotionControlFramePeriod(100);
        mMasterMotor.setControlFramePeriod(ControlFrame.Control_3_General, 10);
        mMasterMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General,
		10, Constants.kCANTimeoutMs);
        mMasterMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0,
		100, Constants.kCANTimeoutMs);
		
		
        // mSlaveMotor = CANSpeedControllerFactory.createDefaultTalonFX(ShooterConstants.kSlaveMotorId);
        mSlaveMotor = new LazyTalonFX(Constants.kShooterFollower2);
		configureTalonFX(mMasterMotor, false, false);
        mSlaveMotor.changeMotionControlFramePeriod(100);
        mSlaveMotor.setControlFramePeriod(ControlFrame.Control_3_General, 10);
        mSlaveMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_1_General,
                10, Constants.kCANTimeoutMs);
        mSlaveMotor.setStatusFramePeriod(StatusFrameEnhanced.Status_2_Feedback0,
                100, Constants.kCANTimeoutMs);

        //TODO: test follow mode
        // mSlaveMotor.follow(mMasterMotor);


        // mMasterMotor.setInverted(TalonFXInvertType.CounterClockwise);
        // mSlaveMotor.setInverted(TalonFXInvertType.CounterClockwise);

        setBrakeMode(false);
		
		reloadGains();
	}

	public enum ShooterControlState {
		OPEN_LOOP,
		VELOCITY,
		IDLE
	}

	// Used to override shooter speed
	public void setOpenLoop(double pow){
        mPeriodicIO.demand = pow;
    }

	public synchronized void setBrakeMode(boolean on) {
            NeutralMode mode = on ? NeutralMode.Brake : NeutralMode.Coast;
            mMasterMotor.setNeutralMode(mode);
            mSlaveMotor.setNeutralMode(mode);
	}

	@Override
	public void onLoop(double timestamp) {
		synchronized(this){
			switch (mControlState){
				case OPEN_LOOP:
					setOpenLoop(1);
					break;
				case VELOCITY:
					// handleDistanceRPM(VisionTracker.getInstance().getTargetDistance());
					setVelocity(kHighVelocity, 0);
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
		mPeriodicIO.timestamp = Timer.getFPGATimestamp();
        mPeriodicIO.position_ticks = mMasterMotor.getSelectedSensorPosition(0);
       
        mPeriodicIO.velocity = ElementMath.tickPer100msToScaledRPM(mMasterMotor.getSelectedSensorVelocity(0), Constants.kShooterEnconderPPR, Constants.kShooterGearRatio);
		// System.out.println("Shooter Velocity: " + mPeriodicIO.velocity);
	}

	@Override
	public void writePeriodicOutputs() {
		if(mControlState == ShooterControlState.OPEN_LOOP){
            mMasterMotor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            mSlaveMotor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
        }else if (mControlState == ShooterControlState.VELOCITY){
            mMasterMotor.set(ControlMode.Velocity, mPeriodicIO.demand);
            mSlaveMotor.set(ControlMode.Velocity, mPeriodicIO.demand);
            
        }else{ //force default Open Loop
            mMasterMotor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            mSlaveMotor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
        }
	}

	public void setVelocity(double demand, double ff){
        if(mControlState != ShooterControlState.VELOCITY){
            configureVelocityTalon();
            mControlState = ShooterControlState.VELOCITY;
        }



        mPeriodicIO.demand = ElementMath.rpmToTicksPer100ms(demand, Constants.kShooterEnconderPPR);
        mPeriodicIO.feedforward = ff;
    }

	@Override
	public void onSimulationLoop() {
	}


	public void setControlState(ShooterControlState state){
        mControlState = state;
    }

	public static Shooter getInstance() {
		if (mInstance == null) {
			mInstance = new Shooter();
		}

		return mInstance;
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
        mPeriodicIO = new ShooterPeriodicIO();
    }

	public double getVelocity()
	{
		return mPeriodicIO.velocity;
	}

	private void configureVelocityTalon(){

        // mMasterMotor.setNeutralMode(NeutralMode.Brake);
        mMasterMotor.selectProfileSlot(0, 0);
    
        mMasterMotor.configClosedloopRamp(0);
    

        System.out.println("Switching shooter to velocity");
    }
}
