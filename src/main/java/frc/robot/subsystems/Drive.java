// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TODO: Create Drive System
public class Drive extends SubsystemBase {
  private Drive mDrive = null;
  
  /** Creates a new Drive. */
  private Drive() {

  }
  
  // Gets drive instance 
  public Drive getInstance() {
    if (mDrive == null)
    {
      mDrive = new Drive();
    }

    return mDrive;
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
