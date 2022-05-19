package com.team4.robot;

public class Constants {
		
    public static final int kCANTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;

    // Controllers
    public static final int kDriverControlSlot = 0;
    public static final int kOperatorControlSlot = 1;
    public static final double kJoystickThreshold = 0.1;

    // Drive Motors
    public static final int kDriveLeftMaster1 = 3;
    public static final int kDriveLeftFollower2 = 1;
    public static final int kDriveRightMaster1 = 4;
    public static final int kDriveRightFollower2 = 2;
	
	//Drive PID values
	public static final double kDriveKP = 0.0;
	public static final double kDriveKI = 0.0;
	public static final double kDriveKD = 0.0;
	

	// Drivetrain Dynamics
    public static final double kDriveWheelTrackWidthInches = 21;
    public static final double kDriveWheelDiameterInches = 4;
    public static final double kDriveWheelCircumferenceInches = Math.PI * kDriveWheelDiameterInches;
    public static final double kDriveWheelRadiusInches = kDriveWheelDiameterInches / 2.0;
    public static final double kDriveTrackScrubFactor = 1.0; 
    public static final double kDriveGearRatio = 504.0 / (3240.0);
    public static final double kDriveEnconderPPR = 2048.0;
    public static final double kFalconStallTorque = 4.69; // documentation
    public static final double kDriveAssumedTorqueEff = 1.0; // Ask B and maybe characterize
    
    // Drive Path Follower
    public static final double kMinLookAhead = 12.0; // inches
    public static final double kMinLookAheadSpeed = 12.0; // inches per second
    public static final double kMaxLookAhead = 48.0; // inches
    public static final double kMaxLookAheadSpeed = 120.0; // inches per second
    public static final double kDeltaLookAhead = kMaxLookAhead - kMinLookAhead;
    public static final double kDeltaLookAheadSpeed = kMaxLookAheadSpeed - kMinLookAheadSpeed;

    public static final double kInertiaSteeringGain = 0.0; 
    public static final double kPathFollowingMaxVel = 5.0 * 12.0; 
    
	// Grab from drive characterize and tune
    public static final double kPathFollowingProfileKp = 0.3 / 12.0;  
    public static final double kPathFollowingProfileKi = 0.0;
    public static final double kPathFollowingProfileKv = 0.01 / 12.0;  
    public static final double kPathFollowingProfileKffv = 0.003889;  
    public static final double kPathFollowingProfileKffa = 0.001415;  
    public static final double kPathFollowingProfileKs = 0.1801 / 12.0;  
    public static final double kPathFollowingGoalPosTolerance = 3.0;
    public static final double kPathFollowingGoalVelTolerance = 12.0;
    public static final double kPathStopSteeringDistance = 12.0;
    
    // Drive PID values
    public static final double kDriveVelocityKP = 0.01;
    public static final double kDriveVelocityKI = 0.0;
    public static final double kDriveVelocityKD = 5.0;
    public static final double kDriveVelocityKF = 0.049;    

    public static final double kDriveDistanceKP = 0.025;
    public static final double kDriveDistanceKI = 0.0;
    public static final double kDriveDistanceKD = 0.0;

    public static final double kDriveAngleKP = 0.0;
    public static final double kDriveAngleKI = 0.0;
    public static final double kDriveAngleKD = 0.0;

    public static final double kDistancePidTolerance = 1.0;

    // Conveyor Motors
    public static final int kConveyorLeft = 10;
    public static final int kConveyorRight = 11;

    // Climber Motors
    public static final int kClimberLeftMotor = 13;
    public static final int kClimberRightMotor = 14;
	
    // Pnuematics
    public static final int kIntakeSolenoid = 5;
    public static final int kClimbSolenoid = 6;
	public static final int kWinchSolenoid = 7;
    public static final int kCompressorID = 1;
	
    // Intake
    public static final int kRollerMotorID = 7;
    public static final int kArmMotorLeftID = 0;
    public static final int kArmMotorRightID = 9;
    //public static final int kIntakeFirst = 8;

    public static final double kIntakeForwardPower = 0.75;
    public static final double kIntakeReversePower = -0.75;
    public static final double kIntakeOff = 0d;
	
    // Conveyor
    public static final double kConveyorForwardPower = 0.3;
    public static final double kConveyorReversePower = -0.35;
    public static final double kConveyorOff = 0;

    // Shooter
    public static final int kShooterBottom = 6; 
    public static final int kShooterTop = 5; 

	public static final double kHighVelocityBottom = 3700 * 1.1;
	public static final double kHighVelocityTop = 3500 * 1.1;
	public static final double kLowVelocityBottom = 600; 
	public static final double kLowVelocityTop = 400;

	public static final double kShooterBottomGearRatio = 48.0/32.0; 
	public static final double kShooterTopGearRatio = 50.0/30.0;

	//Shooter PID
	public static final double kShooterBottomKp = 0.115;
	public static final double kShooterBottomKi = 0.0;
	public static final double kShooterBottomKd = 5.5;
	public static final double kShooterBottomKf = .046;

	public static final double kShooterTopKp = 0.115;
	public static final double kShooterTopKi = 0.0;
	public static final double kShooterTopKd = 5.5;
	public static final double kShooterTopKf = .046;
	
	public static final int kShooterIZone = 0;
	public static final double kShooterRampRate = 0.0;
	public static final double kShooterEnconderPPR = 2048.0;
}

