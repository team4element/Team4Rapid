package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.team4.robot.Constants;

import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

class ClimberPeriodicIO implements Loggable
{
    @Log
    public double demand;
}



public class Climber extends Subsystem {

    public enum ClimberControlState{
        CLIMB_UP,
        CLIMB_DOWN,
        IDLE
    }

    private static Climber mInstance = null;

    private final TalonFX mLeftMotor, mRightMotor;
    private ClimberControlState mControlState = ClimberControlState.IDLE;
    private ClimberPeriodicIO mPeriodicIO = new ClimberPeriodicIO();
    public static Climber getInstance()
    {
        if (mInstance == null)
        {
            mInstance = new Climber();
        }
    
        return mInstance;
    }


    private Climber()
    {
        mLeftMotor = new TalonFX(Constants.kClimberLeftMotor);
        mRightMotor = new TalonFX(Constants.kClimberRightMotor);
        mRightMotor.follow(mLeftMotor);
    }

    @Override
    public void readPeriodicInputs() {
        // TODO Auto-generated method stub
        
    }

    public void setClimb(ClimberControlState controlState)
    {
        mControlState = controlState;
    }

    @Override
    public void writePeriodicOutputs() {
        mLeftMotor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
    }

    @Override
    public void onLoop(double timestamp) {
        switch(mControlState){
            case CLIMB_UP:
                mPeriodicIO.demand = 1;
                break;
            case CLIMB_DOWN:
                mPeriodicIO.demand = -1;
                break;
            case IDLE:
                mPeriodicIO.demand = 0;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDisableLoop() {
        mControlState = ClimberControlState.IDLE;
        
    }
    
}
