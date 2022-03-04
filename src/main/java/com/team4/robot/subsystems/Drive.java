// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team4.robot.subsystems;

import java.util.HashSet;
import java.util.Set;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.StatusFrame;
import com.ctre.phoenix.motorcontrol.TalonFXFeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXSimCollection;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team254.lib.util.DriveSignal;
import com.team4.lib.drivers.TalonFactory;
import com.team4.lib.drivers.TalonUtil;
import com.team4.robot.Constants;
import com.team4.robot.Robot;
import com.team4.robot.subsystems.states.TalonControlState;

import edu.wpi.first.math.kinematics.DifferentialDriveOdometry;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.simulation.AnalogGyroSim;
import edu.wpi.first.wpilibj.simulation.DifferentialDrivetrainSim;
import edu.wpi.first.wpilibj.smartdashboard.Field2d;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

/**
 * A drivetrain consists of multiple motors that are connected to a single
 * gearbox.
 * A drivetrain allows for Open-Loop control as well as closed-loop control.
 */

/**
 * This represents ALL of the Subsystem-related Inputs and Outputs.
 * This includes Motor Signals, and Sensor Signals
 */
class DrivePeriodicIO implements Loggable {
	// Desired Demands on the motor signals
	@Log
	public double left_demand;
	@Log
	public double right_demand;

	// Sensor Inputs

	// Robot State Outputs (useful for high-level control)
	@Log
	public double left_feedforward;
	public double right_feedforward;
}

public class Drive extends Subsystem {
	private static Drive mInstance = null;

	// Motors that controls wheels
	private final WPI_TalonFX mLeftMaster1, mleftFollower2;
	private final WPI_TalonFX mRightMaster1, mRightFollower2;

	private final Set<WPI_TalonFX> mLeftSide = new HashSet<>();
	private final Set<WPI_TalonFX> mRightSide = new HashSet<>();

	private static enum DriveControlState {
		OPEN_LOOP
	}

	private DriveControlState mDriveControlState;
	private TalonControlState mTalonControlState;

	@Log
	private boolean mIsBrakeMode;

	private DrivePeriodicIO mPeriodicIO;
	private DifferentialDrivetrainSim m_driveSim;
	private TalonFXSimCollection m_leftDriveSim;
	private TalonFXSimCollection m_rightDriveSim;

	// TODO: Need to switch to NavX. Might not have an easy Sim to use.
	AnalogGyro m_gyro = new AnalogGyro(1);
	AnalogGyroSim m_gyroSim = new AnalogGyroSim(m_gyro);

	// TODO: Where did we find documentation of this?
	final int kCountsPerRev = 4096;
	final double kSensorGearRatio = 1;
	final double kGearRatio = 10.71;
	final double kWheelRadiusInches = 3;
	final int k100msPerSecond = 10;

	Field2d m_field;
	DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d());

	public static void configureTalonFX(WPI_TalonFX talon, boolean left, boolean main_encoder_talon) {
		talon.setInverted(!left);
		// general
		// This is a manual override to enable drivetrain simulation to work.
		// Simulation will be very useful for debugging auton modes.
		if (Robot.isSimulation()) {
			talon.setInverted(false);
		}

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

	/** Creates a new Drive. */
	private Drive() {
		mPeriodicIO = new DrivePeriodicIO();

		// Starts all Talons in Coast Mode
		mLeftMaster1 = TalonFactory.createDefaultTalonFX(Constants.kDriveLeftMaster1);
		configureTalonFX(mLeftMaster1, true, true);

		mleftFollower2 = TalonFactory.createPermanentSlaveTalonFX(Constants.kDriveLeftFollower2, mLeftMaster1);
		configureTalonFX(mleftFollower2, true, false);

		// This is a convenience method to act upon all the motors on the left side of
		// the drivetrain.
		mLeftSide.add(mLeftMaster1);
		mLeftSide.add(mleftFollower2);

		mRightMaster1 = TalonFactory.createDefaultTalonFX(Constants.kDriveRightMaster1);
		configureTalonFX(mRightMaster1, false, true);

		mRightFollower2 = TalonFactory.createPermanentSlaveTalonFX(Constants.kDriveRightFollower2, mRightMaster1);
		configureTalonFX(mRightFollower2, false, false);

		mRightSide.add(mRightMaster1);
		mRightSide.add(mRightFollower2);

		mIsBrakeMode = false;
		setBrakeMode(false);


		m_driveSim = new DifferentialDrivetrainSim(
				DCMotor.getFalcon500(2),
				kGearRatio,
				Constants.kRobotMOI, // MOI of robot
				Constants.kRobotMass, // Robot Mass
				Units.inchesToMeters(Constants.kDriveWheelRadiusInches),
				Units.inchesToMeters(Constants.kDriveWheelTrackWidthInches),
				null // Measurement Noise
		);
		m_leftDriveSim = mLeftMaster1.getSimCollection();
		m_rightDriveSim = mRightMaster1.getSimCollection();

		m_field = new Field2d();

		SmartDashboard.putData("Field", m_field);
	}

	// Singleton-pattern
	public static Drive getInstance() {
		if (mInstance == null) {
			mInstance = new Drive();
		}

		return mInstance;
	}

	/**
	 * https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#neutral-mode
	 * https://firstwiki.github.io/wiki/talon-sr#:~:text=In%20'brake'%20mode%2C%20the,stops%20can%20cause%20tipping%20issues.
	 * The Neutral Mode determines the TalonFX behavior when no signal is supplied.
	 * 
	 * Coast Mode: When no signal is applied, the TalonFX will continue to freely
	 * spin to a halt.
	 * Brake Mode: When no signal is applied, the TalonFX will halt immediately. It
	 * does this by "shorting" the output terminals together.
	 * 
	 * For example, while driving in open loop control, you want the robot to
	 * "coast" if no inputs are supplied.
	 * Maybe for Autonomous, you want the robot to "brake" if no inputs are
	 * supplied.
	 */
	public synchronized void setBrakeMode(boolean shouldEnable) {
		if (mIsBrakeMode != shouldEnable) {
			mIsBrakeMode = shouldEnable;

			// TODO: Add helper method to loop over all Talons
			mLeftMaster1.setNeutralMode(mIsBrakeMode ? NeutralMode.Brake : NeutralMode.Coast);
			mleftFollower2.setNeutralMode(mIsBrakeMode ? NeutralMode.Brake : NeutralMode.Coast);
			mRightMaster1.setNeutralMode(mIsBrakeMode ? NeutralMode.Brake : NeutralMode.Coast);
			mRightFollower2.setNeutralMode(mIsBrakeMode ? NeutralMode.Brake : NeutralMode.Coast);
		}
	}

	/**
	 * When the Talons are set to "coast", it should also have a minimum
	 * time to go from 0 to full throttle. This might prevent traction issues.
	 */
	private void configureOpenTalon() {
		setBrakeMode(false);

		mLeftMaster1.configOpenloopRamp(0.25);
		mRightMaster1.configOpenloopRamp(0.25);
		mTalonControlState = TalonControlState.OPEN;
	}

	public synchronized void setOpenLoop(DriveSignal signal) {
		if (mDriveControlState != DriveControlState.OPEN_LOOP) {
			mDriveControlState = DriveControlState.OPEN_LOOP;
		}

		if (mTalonControlState != TalonControlState.OPEN) {
			configureOpenTalon();
		}

		mPeriodicIO.left_demand = signal.getLeft();
		mPeriodicIO.right_demand = signal.getRight();
		mPeriodicIO.left_feedforward = 0.0;
		mPeriodicIO.right_feedforward = 0.0;
	}

	@Override
	public void readPeriodicInputs() {
		// While enabled, gets information from sensors
		double newLeftPositionMeters = nativeUnitsToDistanceMeters(mLeftMaster1.getSelectedSensorPosition());
		double newRightPositionMeters = nativeUnitsToDistanceMeters(mRightMaster1.getSelectedSensorPosition());

		m_odometry.update(m_gyro.getRotation2d(),
				newLeftPositionMeters,
				newRightPositionMeters);

		m_field.setRobotPose(m_odometry.getPoseMeters());

	}

	/**
	 * Handles writing internal state to the motors
	 */
	@Override
	public synchronized void writePeriodicOutputs() {
		// In the open loop control, set demand on each of the talons as percent output
		if (mDriveControlState == DriveControlState.OPEN_LOOP) {
			mLeftSide.forEach(t -> t.set(ControlMode.PercentOutput, mPeriodicIO.left_demand));
			mRightSide.forEach(t -> t.set(ControlMode.PercentOutput, mPeriodicIO.right_demand));

		}
		SmartDashboard.putData("Field", m_field);
	}

	@Override
	public void onDisableLoop() {
		// TODO set motors to 0
	}

	@Override
	public void onLoop(double timestamp) {
	}

	public void onSimulationLoop() {
		m_driveSim.setInputs(mLeftMaster1.getMotorOutputVoltage(), mRightMaster1.getMotorOutputVoltage());
		m_driveSim.update(Constants.kLoopTime);

		m_leftDriveSim.setIntegratedSensorRawPosition(distanceToNativeUnits(m_driveSim.getLeftPositionMeters()));
		m_leftDriveSim.setIntegratedSensorVelocity(velocityToNativeUnits(m_driveSim.getLeftVelocityMetersPerSecond()));
		m_rightDriveSim.setIntegratedSensorRawPosition(distanceToNativeUnits(m_driveSim.getRightPositionMeters()));
		m_rightDriveSim.setIntegratedSensorVelocity(velocityToNativeUnits(m_driveSim.getRightVelocityMetersPerSecond()));

		m_gyroSim.setAngle(-m_driveSim.getHeading().getDegrees());

		m_leftDriveSim.setBusVoltage(RobotController.getBatteryVoltage());
		m_rightDriveSim.setBusVoltage(RobotController.getBatteryVoltage());
	}

	private int distanceToNativeUnits(double positionMeters) {
		double wheelRotations = positionMeters / (2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches));
		double motorRotations = wheelRotations * kSensorGearRatio;
		int sensorCounts = (int) (motorRotations * kCountsPerRev);
		return sensorCounts;
	}

	private int velocityToNativeUnits(double velocityMetersPerSecond) {
		double wheelRotationsPerSecond = velocityMetersPerSecond / (2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches));
		double motorRotationsPerSecond = wheelRotationsPerSecond * kSensorGearRatio;
		double motorRotationsPer100ms = motorRotationsPerSecond / k100msPerSecond;
		int sensorCountsPer100ms = (int) (motorRotationsPer100ms * kCountsPerRev);
		return sensorCountsPer100ms;
	}

	private double nativeUnitsToDistanceMeters(double sensorCounts) {
		double motorRotations = (double) sensorCounts / kCountsPerRev;
		double wheelRotations = motorRotations / kSensorGearRatio;
		double positionMeters = wheelRotations * (2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches));
		return positionMeters;
	}
}
