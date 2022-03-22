package com.team4.robot;

import com.team4.lib.auto.AutoExecutor;
import com.team4.lib.util.DriveHelper;
import com.team4.robot.automodes.DoNothingMode;
import com.team4.robot.controllers.TeleopControls;
import com.team4.robot.subsystems.Climber;
import com.team4.robot.subsystems.Conveyor;
import com.team4.robot.subsystems.Drive;
import com.team4.robot.subsystems.Intake;
import com.team4.robot.subsystems.Shooter;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;

public class Robot extends TimedRobot {
  
  private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();

  //subsystems
  public static Drive mDrive = new Drive();
	public static Intake mIntake = new Intake();
	public static Shooter mShooter = new Shooter();
  public static Conveyor mConveyor = new Conveyor();
  public static Climber mClimber = new Climber();
  DriveHelper mDriveHelper = DriveHelper.getInstance();

  TeleopControls mTeleopControls = new TeleopControls();

  // Controllers

	public static Compressor mCompressor = new Compressor(Constants.kCompressorID, PneumaticsModuleType.CTREPCM);
  AutoExecutor mAutoExecutor = new AutoExecutor();

  
	

  /**
   * Entered when the robot first starts up.
   * Example Usage: Initialization of any subsystems
   */
  @Override
  public void robotInit() {
    mSubsystemManager.setSubsystems(
        mDrive,
				mIntake,
				mShooter,
        mConveyor,
        mClimber
    );
  }

  /**
   * This is run periodically every loop cycle, regardless of the mode.
   * Example Usage: Diagnostics to be run for each mode.
   * Details: THis is run AFTER the mode specific periodic method, but before
   * SmartDashboard is updated.
   */
  @Override
  public void robotPeriodic() {
  }

  @Override
  public void autonomousInit() {
    mAutoExecutor.start();
    mSubsystemManager.onDisabledStop();
    mSubsystemManager.onEnabledStart();
  }

  /**
   * This is run periodically when Autonomous is running.
   * Example Usage: Any periodic updates to the robot that should happen while
   * Autonomous is running.
   * Details: If using the default Notifier, then periodic robot calls should be
   * placed here.
   */
  @Override
  public void autonomousPeriodic() { 
    mSubsystemManager.onEnabledLoop();
  }

  /**
   * This is run once when Teleop is entered.
   * Example Usage: Should be used to start any subsystems.
   */
  @Override
  public void teleopInit() {
    System.out.println("Teleop Init!");
    mClimber.resetEncoders();
    mClimber.toggleWinch();
    mSubsystemManager.onDisabledStop();
    mSubsystemManager.onEnabledStart();
  }

  /**
   * This is run periodically when Teleop is running.
   * Example Usage: Modify Subsystem behavior based on input from the Driver
   * Example Usage: Run each Subsystem's periodic behavior.
   */
  @Override
  public void teleopPeriodic() {
    // Controller Inputs
    mTeleopControls.runTeleop();
    mSubsystemManager.onEnabledLoop();
    }
  

  /**
   * This is run once when the robot is disabled.
   * Example Usage: Reset Auto Mode, run any disabled behavior for subsystems
   */
  @Override
  public void disabledInit() {
    mAutoExecutor.stop();
    mSubsystemManager.onEnabledStop();
    mSubsystemManager.onDisabledStart();
  }

  /**
   * This is run periodically when the robot is disabled.
   * Example Usage: Update Auto Modes, reset any subsystems.
   */
  @Override
  public void disabledPeriodic() {
    mSubsystemManager.onDisabledLoop();
    mAutoExecutor.setAutoMode(new DoNothingMode());
  }

  /**
   * This is run once when Test is entered.
   * Example Usage: Should be used to run tests on any subsystems.
   */
  @Override
  public void testInit() {
    mSubsystemManager.onEnabledStop();
    mSubsystemManager.onDisabledStop();
  }

  /**
   * This is run periodically when Test is running.
   */
  @Override
  public void testPeriodic() {
  }

  @Override
  public void simulationPeriodic() {
    mSubsystemManager.onSimulationLoop(); 
  }
}
