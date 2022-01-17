package com.team4.robot;

import com.team4.robot.limelight.constants.LimelightConstants;

public class Constants {
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


    //Limelight
    public static final LimelightConstants kShooterLimelightConsts = new LimelightConstants("Shooter Limelight", "limelight", 0d, 0d, 0d);

}
