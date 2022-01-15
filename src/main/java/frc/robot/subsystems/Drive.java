// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
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
  private final TalonFX mLeftMaster1, mleftFollower2, mleftFollower3;
  private final TalonFX mRightMaster1, mRightFollower2, mRightFollower3;

  
  /** Creates a new Drive. */
  // Constructor
  private Drive() {
    mLeftMaster1 = new TalonFX(Constants.kLeftMaster1);
    mleftFollower2 = new TalonFX(Constants.kLeftFollower2);
    mleftFollower3 = new TalonFX(Constants.kLeftFollower3);

    mleftFollower2.follow(mLeftMaster1);
    mleftFollower3.follow(mLeftMaster1);

    mRightMaster1 = new TalonFX(Constants.kRightMaster1);
    mRightFollower2 = new TalonFX(Constants.kRightFollower2);
    mRightFollower3 = new TalonFX(Constants.kRightFollower3);

    mRightFollower2.follow(mRightMaster1);
    mRightFollower3.follow(mRightMaster1);
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
