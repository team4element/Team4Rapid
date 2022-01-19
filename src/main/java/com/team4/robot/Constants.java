package com.team4.robot;

public class Constants {
    public static final int kCANTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;

    // Controllers
    public static final int kDriverControlSlot = 0;
    public static final int kOperatorControlSlot = 1;

    // Hardware
    public static final int kLeftMaster1 = 1;
    public static final int kLeftFollower2 = 2;
    public static final int kRightMaster1 = 4;
    public static final int kRightFollower2 = 5;


    public static final double kDriveWheelTrackWidthInches = 32;
    public static final double kDriveWheelDiameterInches = 6;
    public static final double kDriveWheelCircumferenceInches = Math.PI * kDriveWheelDiameterInches;
    public static final double kDriveWheelRadiusInches = kDriveWheelDiameterInches / 2.0;
    public static final double kTrackScrubFactor = 1.0;  // Tune me!    
    public static final double kDriveGearRatio = 1/7.29; //On actual bot 12d / 48d, 7.71/1  1/7.71, .129701

		public static final double kRobotMOI = 6; // Moment of inertia of the robot around the center
		public static final double kRobotMass = 56; // Robot Mass (kg)

}
