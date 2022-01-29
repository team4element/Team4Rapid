package com.team4.robot;

public class Constants {
		// TODO: What is this for?
    public static final int kCANTimeoutMs = 10;
    public static final int kLongCANTimeoutMs = 100;

    // Controllers
    public static final int kDriverControlSlot = 0;
    public static final int kOperatorControlSlot = 1;

    // Hardware
    public static final int kDriveLeftMaster1 = 1;
    public static final int kDriveLeftFollower2 = 2;
    public static final int kDriveRightMaster1 = 3;
    public static final int kDriveRightFollower2 = 4;
		public static final int kShooterMaster1 = 5;
		public static final int kShooterFollower2 = 6;
		public static final int kIntakeMaster1 = 7;
		// TODO: Are these Master/Slave?


		// TODO: figure the motor configuration for conveyor
		public static final int kConveyorFirstStageRight = 8;
		public static final int kConveyorFirstStageLeft = 9;
		public static final int kHopperMaster = 10;
		public static final int kConveyorFinalStageBottom = 11;
		public static final int kConveyorFinalStageTop = 12;

		public static final int kClimberMaster1 = 13;
		public static final int kClimberWinchMaster1 = 14;


		public static final int kIntakeSolenoidLeft = 0;
		public static final int kIntakeSolenoidRight = 1;



		// Drivetrain Dynamics
    public static final double kDriveWheelTrackWidthInches = 32;
    public static final double kDriveWheelDiameterInches = 6;
    public static final double kDriveWheelCircumferenceInches = Math.PI * kDriveWheelDiameterInches;
    public static final double kDriveWheelRadiusInches = kDriveWheelDiameterInches / 2.0;
    public static final double kDriveTrackScrubFactor = 1.0;  // Tune me!
    public static final double kDriveGearRatio = 1/7.29; //On actual bot 12d / 48d, 7.71/1  1/7.71, .129701

		// Shooter
		// TODO
		public static final double kShooterTicksPerRevolution = 2048; // based on gear reduction between encoder and output shaft, and encoder ppr

		public static final double kShooterMomentOfInertia = 0.003440853333; // kg * m^2
		public static final double kShooterGearRatio = 1.0; // >1 if flywheel geared down

		// Robot Dynamics
		public static final double kRobotMOI = 6; // Moment of inertia of the robot around the center
		public static final double kRobotMass = 56; // Robot Mass (kg)

		public static final double kJoystickThreshold = 0.04;

		public static final double kLoopTime = 0.020;
}
