package com.team4.robot;

public class Constants {
		
    public static final int kCANTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;

    // Controllers
    public static final int kDriverControlSlot = 0;
    public static final int kOperatorControlSlot = 1;

    // Drive Motors
    public static final int kDriveLeftMaster1 = 3;
    public static final int kDriveLeftFollower2 = 1;
    public static final int kDriveRightMaster1 = 4;
    public static final int kDriveRightFollower2 = 2;
	
	// Shooter Motors
	public static final int kShooterBottom = 6; 
	public static final int kShooterTop = 5; 
	
	//Intake Motors
	public static final int kRollerMotorID = 7;
	public static final int kArmMotorID = 0;
	public static final int kIntakeFirst = 8;
	
	// Conveyor Motors
	public static final int kConveyorLeft = 10;
	public static final int kConveyorRight = 11;

	//Climber Motors
	public static final int kClimberLeftMotor = 13;
	public static final int kClimberRightMotor = 14;
	

	//Pnuematics
	public static final int kIntakeSolenoidLeft = 7;
	public static final int kIntakeSolenoidRight = 4;
	public static final int kClimbSolenoidLeft = 6;
    public static final int kClimbSolenoidRight = 5;
	public static final int kCompressorID = 1;

	//Drive PID values
	public static final double kDriveKP = 0.0;
	public static final double kDriveKI = 0.0;
	public static final double kDriveKD = 0.0;
	

	// Drivetrain Dynamics
    public static final double kDriveWheelTrackWidthInches = 32;
    public static final double kDriveWheelDiameterInches = 6;
    public static final double kDriveWheelCircumferenceInches = Math.PI * kDriveWheelDiameterInches;
    public static final double kDriveWheelRadiusInches = kDriveWheelDiameterInches / 2.0;
    public static final double kDriveTrackScrubFactor = 1.0; 
    public static final double kDriveGearRatio = 1/6.43;
	public static final double kDriveEnconderPPR = 2048.0;

	// Shooter
	public static final double kHighVelocityBottom = 1400;
	public static final double kHighVelocityTop = 1200;
	public static final double kLowVelocityBottom = 600; 
	public static final double kLowVelocityTop = 400;
	public static final double kShooterTicksPerRevolution = 2048; 

	public static final double kShooterMomentOfInertia = 0.003440853333; // kg * m^2
	public static final double kShooterBottomGearRatio = 48.0/32.0; 
	public static final double kShooterTopGearRatio = 50.0/30.0;

	// Intake
	public static final double kIntakeForwardPower = 0.75;
	public static final double kIntakeReversePower = -0.75;
	public static final double kIntakeOff = 0d;
	
	// Conveyor
	public static final double kConveyorForwardPower = 0.3;
	public static final double kConveyorReversePower = -0.35;
	public static final double kConveyorOff = 0;

	// Robot Dynamics
	public static final double kRobotMOI = 6; // Moment of inertia of the robot around the center
	public static final double kRobotMass = 56; // Robot Mass (kg)
	public static final double kJoystickThreshold = 0.1;
	public static final double kLoopTime = 0.020;

	//Shooter PID
	public static final double kShooterBottomKp = 0.0;
	public static final double kShooterBottomKi = 0.0;
	public static final double kShooterBottomKd = 0;
	public static final double kShooterBottomKf = .1;

	public static final double kShooterTopKp = 0.0;
	public static final double kShooterTopKi = 0.0;
	public static final double kShooterTopKd = 0;
	public static final double kShooterTopKf = .1;


	public static final int kShooterIZone = 0;
	public static final double kShooterRampRate = 0.0;
	public static final double kShooterEnconderPPR = 2048.0;

}