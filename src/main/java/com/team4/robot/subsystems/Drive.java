// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXSimCollection;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team254.lib.drivers.TalonFXFactory;
import com.team254.lib.util.DriveSignal;
import com.team4.robot.Constants;
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

import java.util.HashSet;
import java.util.Set;

/**
 * A drivetrain consists of two motors that are connected to a single gearbox.
 */

public class Drive extends Subsystem {
  private static Drive mDrive = null;

  // Motors that controls wheels
  private final WPI_TalonFX mLeftMaster1, mleftFollower2;
  private final WPI_TalonFX mRightMaster1, mRightFollower2;

  private final Set<TalonFX> mLeftSide = new HashSet<>();
  private final Set<TalonFX> mRightSide = new HashSet<>();


  private static enum DriveControlState {
    OPEN_LOOP,
  }

  private DriveControlState mDriveControlState;
  private TalonControlState mTalonControlState;

  private boolean mIsBrakeMode;


  private PeriodicIO mPeriodicIO;
  private DifferentialDrivetrainSim m_driveSim;
  private TalonFXSimCollection m_leftDriveSim;
  private TalonFXSimCollection m_rightDriveSim;
  //Use a standard analog gyro since Pigeon doesn't have sim support yet
  AnalogGyro m_gyro = new AnalogGyro(1);
  AnalogGyroSim m_gyroSim = new AnalogGyroSim(m_gyro);

  final int kCountsPerRev = 4096;
  final double kSensorGearRatio = 1;
  final double kGearRatio = 10.71;
  final double kWheelRadiusInches = 3;
  final int k100msPerSecond = 10;

  Field2d m_field;
  DifferentialDriveOdometry m_odometry = new DifferentialDriveOdometry(m_gyro.getRotation2d());

  /** Creates a new Drive. */
  // Constructor
  private Drive() {
    // Starts all Talons in Open-Loop Mode
    // TODO: Ensure all are in open-loop mode
    mLeftMaster1 = TalonFXFactory.createDefaultTalon(Constants.kLeftMaster1);
    mleftFollower2 = TalonFXFactory.createPermanentSlaveTalon(Constants.kLeftFollower2, Constants.kLeftMaster1);

    mLeftSide.add(mLeftMaster1);
    mLeftSide.add(mleftFollower2);

    mRightMaster1 = TalonFXFactory.createDefaultTalon(Constants.kRightMaster1);
    mRightFollower2 = TalonFXFactory.createPermanentSlaveTalon(Constants.kRightFollower2, Constants.kRightMaster1);

    mRightSide.add(mRightMaster1);
    mRightSide.add(mRightFollower2);

    mIsBrakeMode = false;
    setBrakeMode(false);

    // Container which stores all the Inputs and Outputs to the System
    mPeriodicIO = new PeriodicIO();

    // TODO: Add constants later
    m_driveSim = new DifferentialDrivetrainSim(
      DCMotor.getFalcon500(2),
      kGearRatio,
      2.1, // MOI of robot
      26.5, // Robot Mass
      Units.inchesToMeters(kWheelRadiusInches), // Wheel Radius
      0.546, // Track Width (meters)
      null // Measurement Noise
    );
    m_leftDriveSim = mLeftMaster1.getSimCollection();
    m_rightDriveSim = mRightMaster1.getSimCollection();

    m_field = new Field2d();

    SmartDashboard.putData("Field", m_field);
  }


  // Singleton-pattern
  public static Drive getInstance() {
    if (mDrive == null) {
      mDrive = new Drive();
    }

    return mDrive;
  }


  /**
   * This represents ALL of the Subsystem-related Inputs and Outputs.
   * This includes Motor Signals, and Sensor Signals
   */
  public static class PeriodicIO {
    // Desired Demands on the motor signals
    public double left_demand;
    public double right_demand;

    // Sensor Inputs

    // Robot State Outputs (useful for high-level control)
    public double left_feedforward;
    public double right_feedforward;
  }

  /**
   * https://docs.ctre-phoenix.com/en/stable/ch13_MC.html#neutral-mode
   * https://firstwiki.github.io/wiki/talon-sr#:~:text=In%20'brake'%20mode%2C%20the,stops%20can%20cause%20tipping%20issues.
   * The Neutral Mode determines the TalonFX behavior when no signal is supplied.
   * 
   * Coast Mode: When no signal is applied, the TalonFX will continue to freely spin to a halt.
   * Brake Mode: When no signal is applied, the TalonFX will halt immediately. It does this by "shorting" the output terminals together.
   * 
   * For example, while driving in open loop control, you want the robot to "coast" if no inputs are supplied.
   * Maybe for Autonomous, you want the robot to "brake" if no inputs are supplied.
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
    System.out.println("Switching Talons to Open Loop");

    mLeftMaster1.configOpenloopRamp(0.25);
    mRightMaster1.configOpenloopRamp(0.25);
    mTalonControlState = TalonControlState.OPEN;
  }

  public synchronized void setOpenLoop(DriveSignal signal) {
    if (mDriveControlState != DriveControlState.OPEN_LOOP) {
      System.out.println("switching to open loop");
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

  /**
   * Handles writing internal state to the motors
   */
  public synchronized void writePeriodicOutputs() {
    // In the open loop control, set demand on each of the talons as percent output
    if (mDriveControlState == DriveControlState.OPEN_LOOP) {
      // System.out.println("Left Demand: " + mPeriodicIO.left_demand);
      // System.out.println("Right Demand: " + mPeriodicIO.right_demand);

      mLeftSide.forEach(t -> t.set(ControlMode.PercentOutput, mPeriodicIO.left_demand));
      mRightSide.forEach(t -> t.set(ControlMode.PercentOutput, mPeriodicIO.right_demand));

      SmartDashboard.putData("Field", m_field);
    }
  }

  public void onLoop() {

    m_odometry.update(m_gyro.getRotation2d(),
    nativeUnitsToDistanceMeters(mLeftMaster1.getSelectedSensorPosition()),
    nativeUnitsToDistanceMeters(mRightMaster1.getSelectedSensorPosition()));
    m_field.setRobotPose(m_odometry.getPoseMeters());
  }

  public void onSimulationLoop() {
    // System.out.println("Simulating Drive" + mLeftMaster1.getMotorOutputVoltage() + mRightMaster1.getMotorOutputVoltage());
    m_driveSim.setInputs(mLeftMaster1.getMotorOutputVoltage(), mRightMaster1.getMotorOutputVoltage());
    m_driveSim.update(0.02);

    m_leftDriveSim.setIntegratedSensorRawPosition(distanceToNativeUnits(m_driveSim.getLeftPositionMeters()));
    m_leftDriveSim.setIntegratedSensorVelocity(velocityToNativeUnits(m_driveSim.getLeftVelocityMetersPerSecond()));
    m_rightDriveSim.setIntegratedSensorRawPosition(distanceToNativeUnits(m_driveSim.getRightPositionMeters()));
    m_rightDriveSim.setIntegratedSensorVelocity(velocityToNativeUnits(m_driveSim.getRightVelocityMetersPerSecond()));

    m_gyroSim.setAngle(-m_driveSim.getHeading().getDegrees());

    m_leftDriveSim.setBusVoltage(RobotController.getBatteryVoltage());
    m_rightDriveSim.setBusVoltage(RobotController.getBatteryVoltage());
  }

  private int distanceToNativeUnits(double positionMeters){
    double wheelRotations = positionMeters/(2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches));
    double motorRotations = wheelRotations * kSensorGearRatio;
    int sensorCounts = (int)(motorRotations * kCountsPerRev);
    return sensorCounts;
  }

  private int velocityToNativeUnits(double velocityMetersPerSecond){
    double wheelRotationsPerSecond = velocityMetersPerSecond/(2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches));
    double motorRotationsPerSecond = wheelRotationsPerSecond * kSensorGearRatio;
    double motorRotationsPer100ms = motorRotationsPerSecond / k100msPerSecond;
    int sensorCountsPer100ms = (int)(motorRotationsPer100ms * kCountsPerRev);
    return sensorCountsPer100ms;
  }

  private double nativeUnitsToDistanceMeters(double sensorCounts){
    double motorRotations = (double)sensorCounts / kCountsPerRev;
    double wheelRotations = motorRotations / kSensorGearRatio;
    double positionMeters = wheelRotations * (2 * Math.PI * Units.inchesToMeters(kWheelRadiusInches));
    return positionMeters;
  }
}
