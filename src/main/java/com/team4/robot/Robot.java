// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team4.robot;

import com.team4.lib.util.DriveHelper;
import com.team4.robot.controllers.DriverController;
import com.team4.robot.controllers.OperatorController;
import com.team4.robot.subsystems.Climber;
import com.team4.robot.subsystems.Conveyor;
import com.team4.robot.subsystems.Climber.ClimberControlState;
import com.team4.robot.subsystems.Drive;
import com.team4.robot.subsystems.Intake;
import com.team4.robot.subsystems.Shooter;
import com.team4.robot.subsystems.Shooter.ShooterControlState;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.TimedRobot;
import io.github.oblarg.oblog.Logger;

/**
 * This is the root Robot class.
 * It is responsible for managing function calls on different Match States.
 * The possible Match States are as follows:
 * 1. Disabled
 * 2. Autonomous
 * 3. Teleop
 * 4. Test
 * 5. Simulation
 * 6. Robot
 * 
 * Each of these above states have associated enter/periodic methods.
 * 
 * For example, when entering Teleop, the `teleopInit` method is called.
 * When telop is running, the `teleopPeriodic` method is called each loop
 * (default 20ms refresh rate).
 */
public class Robot extends TimedRobot {
  // Subsystems
  private final SubsystemManager mSubsystemManager = SubsystemManager.getInstance();

  Drive mDrive = Drive.getInstance();
	Intake mIntake = new Intake();
	Shooter mShooter = Shooter.getInstance();
  Conveyor mConveyor = new Conveyor();
  Climber mClimber = new Climber();
  DriveHelper mDriveHelper = DriveHelper.getInstance();

  // Controllers
  DriverController mDriverController = new DriverController();
  OperatorController mOperatorController = new OperatorController();
	Compressor mCompressor = new Compressor(Constants.kCompressorID, PneumaticsModuleType.CTREPCM);
  AutonExecute mAutonExecute= new AutonExecute();

  /**
   * Entered when the robot first starts up.
   * Example Usage: Initialization of any subsystems
   */
  @Override
  public void robotInit() {
		Logger.configureLoggingAndConfig(this, false);
    mSubsystemManager.setSubsystems(
        mDrive,
				mIntake,
				mShooter,
        mConveyor,
        mClimber
    );

		  //mCompressor.enableDigital();
  }

  /**
   * This is run periodically every loop cycle, regardless of the mode.
   * Example Usage: Diagnostics to be run for each mode.
   * Details: THis is run AFTER the mode specific periodic method, but before
   * SmartDashboard is updated.
   */
  @Override
  public void robotPeriodic() {
		Logger.updateEntries();
    // Add try catch like 254 does.
  }

  /**
   * This is run once when Autonomous is entered.
   * Example Usage: Picking the relevant autonomous mode using SendableChooser
   * 
   * TODO: Add autonomous mode selection
   */
  @Override
  public void autonomousInit() {
    mAutonExecute.start();
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
    mAutonExecute.periodic();
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
    double throttle = mDriverController.getThrottle();
    double turn = mDriverController.getTurn();

		boolean isDeployIntake = mDriverController.getDeployIntake();
		boolean isShooterOn = mOperatorController.getIsShooterOn();
    

    mDrive.setOpenLoop(mDriveHelper.elementDrive(throttle, turn, false));



		if (isDeployIntake) {
			mIntake.moveArm();
		} 
		
		if (mOperatorController.getCompressorToggle()) {

			mCompressor.enableDigital();

		} else {
			mCompressor.disable();
    } 		

		if (mOperatorController.intakeForward()) {
			  mIntake.state = Intake.mState.FORWARD;
		} else if (mOperatorController.intakeBackwards()) {
        mIntake.state = Intake.mState.REVERSE;
		} else {
        mIntake.state = Intake.mState.IDLE;
		}

    if(mOperatorController.conveyorForward()){
      mConveyor.state = Conveyor.mState.FORWARD;
    } else if (mOperatorController.conveyorBackward()){
      mConveyor.state = Conveyor.mState.REVERSE;
    }else{
      mConveyor.state = Conveyor.mState.IDLE;
    }

		if (isShooterOn) {
			mShooter.setControlState(ShooterControlState.VELOCITY);
		} else {
			mShooter.setControlState(ShooterControlState.IDLE);;
		}

    if(mDriverController.changeWinch()){
      mClimber.toggleWinch();
    }
    
    if (mDriverController.getClimbUp())
    {
      mClimber.setClimb(ClimberControlState.CLIMB_UP);
    }
    else if (mDriverController.getClimbDown())
    {
      mClimber.setClimb(ClimberControlState.CLIMB_DOWN);
    }
    else
    {
      mClimber.setClimb(ClimberControlState.IDLE);
    }


    // Run each subsystem's periodic function
    mSubsystemManager.onEnabledLoop();
    }
  

  /**
   * This is run once when the robot is disabled.
   * Example Usage: Reset Auto Mode, run any disabled behavior for subsystems
   */
  @Override
  public void disabledInit() {
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
