// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.team4.robot.subsystems;

import java.util.ArrayList;

import com.team254.lib.geometry.Translation2d;
import com.team254.lib.util.CrashTracker;
import com.team254.lib.util.MovingAverage;
import com.team4.robot.limelight.constants.LimelightConstants;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Limelight extends SubsystemBase 
{
  private LimelightConstants mConstants;

  private final NetworkTable mNetworkTable;
  private PeriodicIO mPeriodicIO;

  public static class PeriodicIO {
    // INPUTS
    public double validTarget;
		public double horizontalDeviation;
		public double verticalDeviation;
		public double area;
		public double skew;
		public double latency;
		public double distance;
		public double cameraA1;
		public double pipelineValue;
		public MovingAverage calculatedSkewFactor = new MovingAverage(10);

		public ArrayList<Translation2d> pointArray = new ArrayList<>();

    public boolean visionEnable = false;


    // OUTPUTS
    public int ledMode = 1; // 0 - use pipeline mode, 1 - off, 2 - blink, 3 - on
    public int camMode = 0; // 0 - vision processing, 1 - driver camera
    public int pipeline = 0; // 0 - 9
    public int stream = 2; // sets stream layout if another webcam is attached
    public int snapshot = 0; // 0 - stop snapshots, 1 - 2 Hz

    
  }

  public Limelight(LimelightConstants constants)
  {
    mConstants = constants;
    mNetworkTable = NetworkTableInstance.getDefault().getTable(mConstants.getTableName());

    mPeriodicIO = new PeriodicIO();
  }

  public synchronized void readPeriodicInputs() 
  {
    mPeriodicIO.pipelineValue = mNetworkTable.getEntry("getpipe").getDouble(0);
    
    if (mPeriodicIO.visionEnable)
    {
      mPeriodicIO.validTarget = mNetworkTable.getEntry("tv").getDouble(0);
      mPeriodicIO.horizontalDeviation = mNetworkTable.getEntry("tx").getDouble(0);
      mPeriodicIO.verticalDeviation = mNetworkTable.getEntry("ty").getDouble(0);
      mPeriodicIO.area = mNetworkTable.getEntry("ta").getDouble(0);
      mPeriodicIO.skew = mNetworkTable.getEntry("ts").getDouble(0);
      mPeriodicIO.latency = mNetworkTable.getEntry("tl").getDouble(0);
      mPeriodicIO.cameraA1 = Math.toDegrees(Math.atan((mConstants.getTarget() - mConstants.getHeight())/(144) /*240 inches to find what A1 is */))-mPeriodicIO.verticalDeviation;
      mPeriodicIO.pipeline = 0;
      mPeriodicIO.ledMode = 3;
      /**
       * This distance value potentially needs to be tuned
       * 
       * to find the the value to mutliple this equation by
       * place the bot at 20 known distance points and log what the robot outputs
       * in comparison to what it is actually at
       * 
       * From there find the average deviation and divide that by the average distance to find your modifier
       * Follow that by putting mPeriodicIO.distance *= modifier;
       * 
       * This leaves you with more accurate distance
       */
      mPeriodicIO.distance =
      (mConstants.getTarget() - mConstants.getHeight()) /
      /*	Math.toDegrees(*/Math.tan(Math.toRadians(mConstants.getAngle() + mPeriodicIO.verticalDeviation))/*)*/;
      

    }
    else
    {
      mPeriodicIO.validTarget = 0;
      mPeriodicIO.horizontalDeviation = 0;
      mPeriodicIO.verticalDeviation = 0;
      mPeriodicIO.area = 0;
      mPeriodicIO.skew = 0;
      mPeriodicIO.latency = 0;
      mPeriodicIO.cameraA1 = 0;

      mPeriodicIO.pipeline = 1;
      mPeriodicIO.ledMode = 1;
    }

    
  }

  public synchronized double getDistance()
  {
    return mPeriodicIO.distance;
  }


  public synchronized void writePeriodicOutputs()
  {
    // This method will be called once per scheduler run
    try {
			mNetworkTable.getEntry("pipeline").setNumber(mPeriodicIO.pipeline);
			mNetworkTable.getEntry("ledMode").setNumber(mPeriodicIO.ledMode);
		}
		catch (Exception ex) {
			CrashTracker.logThrowableCrash(ex);
		}
  }

  public void setVision(boolean enable)
  {
    mPeriodicIO.visionEnable = enable;
  }

  public void onLoop() {
    readPeriodicInputs();
    writePeriodicOutputs();
  }
}
