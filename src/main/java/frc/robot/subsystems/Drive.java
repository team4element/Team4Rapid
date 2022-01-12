// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// TODO: Create Drive System
public class Drive extends SubsystemBase
{
  private Drive mDrive = null;

    /** Creates the Drive Train */
  private Drive()
  {
  
  }

  /**
   * Gets the current instance of the drive base
   * Use this rather than calling "new Drive()" every time when using the drive
   * train to save on memory
   *
   * @return the current instance of drive
   */
  public Drive getInstance()
  {
      if (mDrive == null)
      {
          mDrive = new Drive();
      }

      return mDrive;
  }

  @Override 
  public void periodic()
  {
      // This method will be called once per scheduler run
  }
}
