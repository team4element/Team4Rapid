// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team4.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase 
{
  private Limelight mInstance = null;


  /** Creates a new Limelight. */
  private Limelight() 
  {}

  public Limelight getInstance()
  {
    if (mInstance == null)
    {
      mInstance = new Limelight();
    }

    return mInstance;
  }


  @Override
  public void periodic() 
  {
    // This method will be called once per scheduler run
  }
}
