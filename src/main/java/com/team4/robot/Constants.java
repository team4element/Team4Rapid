package com.team4.robot;

import com.team254.lib.physics.DCMotorTransmission;
import com.team254.lib.physics.DifferentialDrive;

import edu.wpi.first.math.util.Units;

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

	// Drivetrain Dynamics
    public static final double kDriveWheelTrackWidthInches = 32;
    public static final double kDriveWheelDiameterInches = 4;
    public static final double kDriveWheelCircumferenceInches = Math.PI * kDriveWheelDiameterInches;
    public static final double kDriveWheelRadiusInches = kDriveWheelDiameterInches / 2.0;
    public static final double kDriveTrackScrubFactor = 1.0; 
    public static final double kDriveGearRatio = 504.0 / (3240.0);
    public static final double kDriveEnconderPPR = 2048.0;
    public static final double kFalconStallTorque = 4.69; // documentation
    public static final double kDriveAssumedTorqueEff = 1.0; // Ask B and maybe characterize
    // Characterize drive for the following
    public static final double kDriveVIntercept = 0.0;
    public static final double kDriveKV = 0.0; 
    public static final double kDriveLinearInertia = 65.0; //find actual value 
    public static final double kDriveLinearKA = 12.0 / (((1.0 / kDriveGearRatio) * kFalconStallTorque * kDriveAssumedTorqueEff * 6) / (kDriveLinearInertia * Math.pow(Units.inchesToMeters(kDriveWheelRadiusInches), 2))); //purely theoretical characterize to find actual 
    public static final double kDriveAngularKA = 0.0; 
    public static final double kDriveAngularInertia = kDriveAngularKA / kDriveLinearKA * Math.pow(Units.inchesToMeters(kDriveWheelTrackWidthInches / 2.0), 2)* kDriveLinearInertia;
    public static final double kDriveAngularDrag = 0.0; // characterize or ask b  
    
    // Drive PID values
    public static final double kDriveVelocityKP = 0.0;
    public static final double kDriveVelocityKI = 0.0;
    public static final double kDriveVelocityKD = 0.0;
    public static final double kDriveVelocityKF = 0.0;    

    public static final double kDriveDistanceKP = 0.025;
    public static final double kDriveDistanceKI = 0.0;
    public static final double kDriveDistanceKD = 0.0;
		
    public static final double kDriveAngleKP = 0.0;
    public static final double kDriveAngleKI = 0.0;
    public static final double kDriveAngleKD = 0.0;

    public static final double kDistancePidTolerance = 1.0;
	
    // Drive Model
    public static final DCMotorTransmission kDriveTransmission = new DCMotorTransmission(
                                    1.0 / kDriveKV, 
                                    Math.pow(Units.inchesToMeters(kDriveWheelRadiusInches), 2.0) * (kDriveLinearInertia / (2.0 * kDriveLinearKA)), 
                                    kDriveVIntercept
                                    );    

    public static final DifferentialDrive kDriveModel = new DifferentialDrive(
                                        kDriveLinearInertia,
                                        kDriveAngularInertia,
                                        kDriveAngularDrag,
                                        Units.inchesToMeters(kDriveWheelRadiusInches),
                                        Units.inchesToMeters(kDriveWheelTrackWidthInches / 2.0 * kDriveTrackScrubFactor),
                                        kDriveTransmission, kDriveTransmission 
                                        );

    // Conveyor Motors
    public static final int kConveyorLeft = 10;
    public static final int kConveyorRight = 11;

    // Climber Motors
    public static final int kClimberLeftMotor = 13;
    public static final int kClimberRightMotor = 14;
	
    // Pnuematics
    public static final int kIntakeSolenoidLeft = 7;
    public static final int kIntakeSolenoidRight = 4;
    public static final int kClimbSolenoidLeft = 6;
    public static final int kClimbSolenoidRight = 5;
    public static final int kCompressorID = 1;
	
    // Intake
    public static final int kRollerMotorID = 7;
    public static final int kArmMotorID = 0;
    public static final int kIntakeFirst = 8;

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

	public static final double kHighVelocityBottom = 3700;
	public static final double kHighVelocityTop = 3500;
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

