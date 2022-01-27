package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXSimCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.robot.Constants;
import com.team4.robot.Robot;

import edu.wpi.first.math.Nat;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.LinearQuadraticRegulator;
import edu.wpi.first.math.estimator.KalmanFilter;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.LinearSystemLoop;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

class ShooterPeriodicIO implements Loggable {
	// Inputs
	// These are in Motor Units
	public double master_rpm_ticks_per_100ms = 0.0;
	public double slave_rpm_ticks_per_100ms = 0.0;

	// These are in rpm units
	public double master_velocity_rpm = 0.0;
	public double slave_velocity_rpm = 0.0;

	public double radiansPerSecond = 0.0;

	// Outputs
	/**
	 * RPM
	 */
	public double demand = 0.0;
}

public class Shooter extends Subsystem<ShooterPeriodicIO> {
	private static Shooter mInstance = null;
	private ShooterPeriodicIO mPeriodicIO;


	private final WPI_TalonFX mMasterMotor, mSlaveMotor;

	private ShooterControlState mControlState = ShooterControlState.OPEN_LOOP;


	/**
	 * This is the Physics Based Controller used to power the flywheel.
	 * It uses a State-Space Model of a spinning mass about an axis.
	 * 
	 * States: RPM of the flywheel.
	 * Inputs: Voltage supplied to motors.
	 * Outputs: RPM of the flywheel
	 */
	private final LinearSystem<N1, N1, N1> mFlywheelPlant;
	private final KalmanFilter<N1, N1, N1> mObserver;
	private final LinearQuadraticRegulator<N1, N1, N1> mController;
	private final LinearSystemLoop<N1, N1, N1> mLoop;

	/**
	 * Objects to simulate the flywheel
	 * 
	 */
	private FlywheelSim mFlywheelSim;
	private TalonFXSimCollection mMasterSim;
	// private Encoder mEncoderHardware;
	// private EncoderSim mEncoderSim;

	private Shooter() {
		mPeriodicIO = new ShooterPeriodicIO();

		mMasterMotor = TalonFactory.createDefaultTalonFX((Constants.kShooterMaster1));
		mMasterMotor.setInverted(false);
		// What is Voltage Compensation for?
		mMasterMotor.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
		// This is set to false so we can set voltages directly from controller.
		mMasterMotor.enableVoltageCompensation(false);

		mSlaveMotor = TalonFactory.createPermanentSlaveTalonFX(Constants.kShooterFollower2, mMasterMotor);
		mSlaveMotor.setInverted(true);
		mSlaveMotor.configVoltageCompSaturation(12.0, Constants.kLongCANTimeoutMs);
		mSlaveMotor.enableVoltageCompensation(false);

		
		mFlywheelPlant = LinearSystemId.identifyVelocitySystem(0.0549, 0.0024487);
		// mFlywheelPlant = LinearSystemId.createFlywheelSystem(DCMotor.getFalcon500(2), Constants.kShooterMomentOfInertia, Constants.kShooterGearRatio);
		mObserver = new KalmanFilter<N1, N1, N1>(
			Nat.N1(), // System State (1D) (rpm)
			Nat.N1(), // Outputs (1D) (rpm)
			mFlywheelPlant, // Plant (Physical Modal)
			VecBuilder.fill(3.0), // Plant Deviation (Q in LQR)
			VecBuilder.fill(0.01), // Measurement Deviation (R in LQR)
			Constants.kLoopTime // Discrete Time Steps
		);
		mController = new LinearQuadraticRegulator<N1, N1, N1>(
			mFlywheelPlant, // Plant
			VecBuilder.fill(500.0), // State Error Tolerance (Q matrix in LQR)
			VecBuilder.fill(12.0), // Measurement Error Tolerance (R matrix in LQR)
			Constants.kLoopTime
		);
		mLoop = new LinearSystemLoop<N1, N1, N1>(
			mFlywheelPlant,
			mController,
			mObserver,
			12.0, // Max Voltage that can be applied
			Constants.kLoopTime
		);


		if (Robot.isSimulation()) {
			mFlywheelSim = new FlywheelSim(
				mFlywheelPlant,
				DCMotor.getFalcon500(1),
				Constants.kShooterGearRatio
			);
			
			mMasterSim = mMasterMotor.getSimCollection();
		}
	}

	public enum ShooterControlState {
		OPEN_LOOP,
		VEOLOCITY,
	}

	// Used to override shooter speed
	public void setOpenLoop(double power) {
		if (mControlState != ShooterControlState.OPEN_LOOP) {
			mControlState = ShooterControlState.OPEN_LOOP;
		}

		mPeriodicIO.demand = power;
	}

	public void setRPM(double desiredRPM) {
		if (mControlState != ShooterControlState.VEOLOCITY) {
			mControlState = ShooterControlState.VEOLOCITY;
		}

		mPeriodicIO.demand = desiredRPM;
	}

	public double getRadiansPerSecond() {
		return (mMasterMotor.getSelectedSensorVelocity() * 10) * (2.0 * Math.PI / Constants.kShooterTicksPerRevolution / Constants.kShooterGearRatio);
	}
	
	@Log(rowIndex=1, columnIndex=0, width=2, height=1, name="RPM")
	public double getRPM() {
		return Units.radiansPerSecondToRotationsPerMinute(getRadiansPerSecond());
	}

	@Override
	public void onEnabledStart() {
		// TODO: What is this and how can I modify this for the actual robot
		// mLoop.reset(VecBuilder.fill(mEncoderHardware.getRate()));
	}
	@Override
	public void onLoop(double timestamp) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisableLoop() {
		// TODO Auto-generated method stub

	}

	@Override
	public void readPeriodicInputs() {
		mPeriodicIO.master_rpm_ticks_per_100ms = mMasterMotor.getSelectedSensorVelocity(0);
		mPeriodicIO.slave_rpm_ticks_per_100ms = mSlaveMotor.getSelectedSensorVelocity(0);

		mPeriodicIO.radiansPerSecond = getRadiansPerSecond();
	}

	@Override
	public void writePeriodicOutputs() {
		if (mControlState == ShooterControlState.OPEN_LOOP) {
			mMasterMotor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
			mSlaveMotor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
		}

		if (mControlState == ShooterControlState.VEOLOCITY) {
			// System.out.println("Demand" + mPeriodicIO.demand);
			mLoop.setNextR(VecBuilder.fill(Units.rotationsPerMinuteToRadiansPerSecond(mPeriodicIO.demand)));
			mLoop.correct(VecBuilder.fill(getRadiansPerSecond()));
			mLoop.predict(Constants.kLoopTime);
			double nextVoltage = mLoop.getU(0);
			mMasterMotor.setVoltage(mPeriodicIO.demand == 0 ? 0 : nextVoltage);
			mSlaveMotor.setVoltage(mPeriodicIO.demand == 0 ? 0 : nextVoltage);
		}
	}

	@Override
	public void onSimulationLoop() {
		mFlywheelSim.setInput(mMasterMotor.get() * RobotController.getInputVoltage());
		mFlywheelSim.update(Constants.kLoopTime);

		double flywheelNativeVelocity = mFlywheelSim.getAngularVelocityRPM() * Constants.kShooterTicksPerRevolution / (60 * 10) * Constants.kShooterGearRatio;
		// System.out.println("RPM: " + Units.radiansPerSecondToRotationsPerMinute(mFlywheelSim.getAngularVelocityRadPerSec()));
		double flywheelNativePositionDelta = flywheelNativeVelocity * 10 * Constants.kLoopTime;

		mMasterSim.setIntegratedSensorVelocity((int) flywheelNativeVelocity);
		mMasterSim.addIntegratedSensorPosition((int) flywheelNativePositionDelta);

		mMasterSim.setBusVoltage(RobotController.getBatteryVoltage());
	}

	public static Shooter getInstance() {
		if (mInstance == null) {
			mInstance = new Shooter();
		}

		return mInstance;
	}
}
