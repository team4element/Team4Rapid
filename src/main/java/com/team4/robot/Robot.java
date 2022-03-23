package com.team4.robot;

import com.team4.lib.auto.AutoExecutor;
import com.team4.lib.util.FieldState;
import com.team4.robot.automodes.DoNothingMode;
import com.team4.robot.controllers.TeleopControls;
import com.team4.robot.subsystems.Climber;
import com.team4.robot.subsystems.Conveyor;
import com.team4.robot.subsystems.Drive;
import com.team4.robot.subsystems.Intake;
import com.team4.robot.subsystems.Shooter;
import com.team4.robot.subsystems.StateEstimator;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  
  private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();
  public static FieldState mFieldState = new FieldState();

  //subsystems
  public static Drive mDrive = new Drive();
	public static Intake mIntake = new Intake();
	public static Shooter mShooter = new Shooter();
  public static Conveyor mConveyor = new Conveyor();
  public static Climber mClimber = new Climber();
  public static StateEstimator mStateEstimator = new StateEstimator();
  
  // others
  public static Compressor mCompressor = new Compressor(Constants.kCompressorID, PneumaticsModuleType.CTREPCM);
  TeleopControls mTeleopControls = new TeleopControls();
  AutoExecutor mAutoExecutor = new AutoExecutor();

  @Override
  public void robotInit() {
    mSubsystemManager.setSubsystems(
        mDrive,
				mIntake,
				mShooter,
        mConveyor,
        mClimber,
        mStateEstimator
    );

    mDrive.resetSensors();
    mFieldState.reset();
  }

  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    mAutoExecutor.start();
    mSubsystemManager.onDisabledStop();
    mSubsystemManager.onEnabledStart();
  }

  @Override
  public void autonomousPeriodic() { 
    mSubsystemManager.onEnabledLoop();
  }

  @Override
  public void teleopInit() {
    System.out.println("Teleop Init!");
    mClimber.resetEncoders();
    mClimber.toggleWinch();
    mSubsystemManager.onDisabledStop();
    mSubsystemManager.onEnabledStart();

    mDrive.resetSensors();
  }

  @Override
  public void teleopPeriodic() {
    // Controller Inputs
    mTeleopControls.runTeleop();
    mSubsystemManager.onEnabledLoop();
  }
  
  @Override
  public void disabledInit() {
    mAutoExecutor.stop();
    mSubsystemManager.onEnabledStop();
    mSubsystemManager.onDisabledStart();
  }

  @Override
  public void disabledPeriodic() {
    mSubsystemManager.onDisabledLoop();
    mAutoExecutor.setAutoMode(new DoNothingMode());
  }

  @Override
  public void testInit() {
    mSubsystemManager.onEnabledStop();
    mSubsystemManager.onDisabledStop();
  }

  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationPeriodic() {
    mSubsystemManager.onSimulationLoop(); 
  }
}
