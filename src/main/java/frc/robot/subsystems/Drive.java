// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.lib.drivers.CANSpeedControllerFactory;
import frc.lib.drivers.TalonUtil;
import frc.robot.Constants;

/**
 * A drivetrain consists of two motors that are connected to a single gearbox.
 * 
 * 
 * 
 */

public class Drive extends SubsystemBase {
  private static Drive mDrive = null;

  // Motors that controls wheels
  private final TalonFX mLeftMaster1, mLeftFollower2, mLeftFollower3;
  private final TalonFX mRightMaster1, mRightFollower2, mRightFollower3;

  
  /** Creates a new Drive. */
  // Constructor
  private Drive() {
    mLeftMaster1 = CANSpeedControllerFactory.createDefaultTalonFX(Constants.kLeftMaster1);
    mLeftFollower2 = CANSpeedControllerFactory.createPermanentSlaveTalonFX(Constants.kLeftFollower2, mLeftMaster1);
    mLeftFollower3 = CANSpeedControllerFactory.createPermanentSlaveTalonFX(Constants.kLeftFollower3, mLeftMaster1);

    // Configures the left talons + sets up talon encoder
    TalonUtil.configureTalonFX(mLeftMaster1, true, true);
    TalonUtil.configureTalonFX(mLeftFollower2, true, false);    
    TalonUtil.configureTalonFX(mLeftFollower3, true, false);    


    mRightMaster1 = CANSpeedControllerFactory.createDefaultTalonFX(Constants.kRightMaster1);
    mRightFollower2 = CANSpeedControllerFactory.createPermanentSlaveTalonFX(Constants.kRightFollower2, mRightMaster1);
    mRightFollower3 = CANSpeedControllerFactory.createPermanentSlaveTalonFX(Constants.kRightFollower3, mRightMaster1);
    
    // Configures the right talons + sets up talon encoder
    TalonUtil.configureTalonFX(mRightMaster1, false, true);
    TalonUtil.configureTalonFX(mRightFollower2, false, false);
    TalonUtil.configureTalonFX(mRightFollower3, false, false);
  }
  
  // Gets drive instance 
  // Singleton-pattern
  public static Drive getInstance() {
    if (mDrive == null) {
      mDrive = new Drive();
    }

    return mDrive;
  }

  /**
   * 
   * If the speeds are the same, it will move front/back
   * If the speeds are different, it was start to turn
   * 
   * If they are both positive, but not the same.
   * 
   * @param throttle
   * @param turn
   */
  public void letsDrive(double throttle, double turnLeft) {
    double leftSpeed = throttle - turnLeft;
    double rightSpeed = throttle + turnLeft;

    mLeftMaster1.set(ControlMode.PercentOutput, leftSpeed);
    mRightMaster1.set(ControlMode.PercentOutput, rightSpeed);
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
