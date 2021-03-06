package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team4.robot.Constants;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Climber extends Subsystem {

    private final TalonFX mLeftMotor, mRightMotor;
	private final Solenoid mWinchPiston;
    private final Solenoid mClimbPiston;
    private ClimberControlState mControlState = ClimberControlState.IDLE;
    private static double kClimbPower = 1;
    private static double kClimbOff = 0;

    public enum ClimberControlState{
        CLIMB_UP,
        CLIMB_DOWN,
        IDLE
    }
    
    public Climber()
    {
        mLeftMotor = new TalonFX(Constants.kClimberLeftMotor);
        mRightMotor = new TalonFX(Constants.kClimberRightMotor);
        
        mRightMotor.follow(mLeftMotor);
        mLeftMotor.setInverted(true);  
        
        mLeftMotor.configReverseSoftLimitEnable(false, 0);
        mRightMotor.configReverseSoftLimitEnable(false, 0);

        mWinchPiston = new Solenoid(1, PneumaticsModuleType.CTREPCM, Constants.kWinchSolenoid);

        mClimbPiston = new Solenoid(1, PneumaticsModuleType.CTREPCM, Constants.kClimbSolenoid);
    }

    @Override
    public void readPeriodicInputs() {
    }

    public void setClimb(ClimberControlState controlState)
    {
        mControlState = controlState;
    }

    @Override
    public void writePeriodicOutputs() {
    }

    @Override
    public void onLoop(double timestamp) {
        switch(mControlState){
            case CLIMB_UP:
                climbUp();
                break;
            case CLIMB_DOWN:
                climbDown();
                break;
            case IDLE:
                motorsOff();
                break;
            default:
                mControlState = ClimberControlState.IDLE;
                break;
        }
    }

    @Override
    public void onDisableLoop() {
        mControlState = ClimberControlState.IDLE;   
    }

    private void climbUp() {
        mLeftMotor.set(ControlMode.PercentOutput, kClimbPower);
    }

    private void climbDown(){
        mLeftMotor.set(ControlMode.PercentOutput, -kClimbPower);
    }

    private void motorsOff(){
        mLeftMotor.set(ControlMode.PercentOutput, kClimbOff);
    }

    public void toggleWinch(){
        mWinchPiston.toggle();
    }

    public void toggleSecondaryClimb(){
        mClimbPiston.toggle();
    }

    public void resetEncoders(){
        mLeftMotor.setSelectedSensorPosition(0);
        mRightMotor.setSelectedSensorPosition(0);
    }

    public double getEncoders(){
        return (mLeftMotor.getSelectedSensorPosition(0) +
            mRightMotor.getSelectedSensorPosition(0)) / 2;
    }
}
