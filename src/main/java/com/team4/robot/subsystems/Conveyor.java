package com.team4.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
import com.team4.lib.drivers.TalonFactory;
import com.team4.robot.Constants;

import edu.wpi.first.wpilibj.DigitalInput;
import io.github.oblarg.oblog.Loggable;
import io.github.oblarg.oblog.annotations.Log;

class ConveyorPeriodicIO implements Loggable
{
    @Log
	public double demand;
    @Log
    public boolean readyToShoot;
    @Log
    public boolean beamBreakTriggered;

}

public class Conveyor extends Subsystem<ConveyorPeriodicIO> {
    private static Conveyor mInstance = null;

    private WantedState mWantedState = WantedState.IDLE;
    private SystemState mSystemState = SystemState.IDLE;

    private final double kSerializeDemand = .75;
    private final double kPrepareDemand = .5;
    private final double kFeedDemand = .75;

    //TODO: Change to actual motor controllers
    private final WPI_TalonFX mFirstStageMaster, mFirstStageSlave;
    private final WPI_TalonFX mHopper;
    private final WPI_TalonFX mFinalStageMaster, mFinalStageSlave; 

    private final DigitalInput mBeamBreak;

    public enum WantedState {
        IDLE,
        SERIALIZE,
        PREPARE_TO_SHOOT,
        FEED
    }

    public enum SystemState {
        IDLE,
        SERIALIZE,
        FEED,
        REBALANCING,
        REBALANCED,
    }

    private Conveyor()
    {
        // TODO: Add constants
        mFirstStageMaster = TalonFactory.createDefaultTalonFX(Constants.kConveyorFirstStageLeft);
        mFirstStageSlave = TalonFactory.createPermanentSlaveTalonFX(Constants.kConveyorFirstStageLeft, mFirstStageMaster);
        
        mHopper = TalonFactory.createDefaultTalonFX(Constants.kHopperMaster);
        
        mFinalStageMaster = TalonFactory.createDefaultTalonFX(Constants.kConveyorFinalStageTop);
        mFinalStageSlave = TalonFactory.createPermanentSlaveTalonFX(Constants.kConveyorFinalStageBottom, mFinalStageMaster);

        mFirstStageMaster.setNeutralMode(NeutralMode.Coast);
        mHopper.setNeutralMode(NeutralMode.Coast);
        mFinalStageMaster.setNeutralMode(NeutralMode.Coast);
        
        mBeamBreak = new DigitalInput(0);

    }

    public static Conveyor getInstance()
    {
        if(mInstance == null)
        {
            mInstance = new Conveyor();
        }

        return mInstance;
    }

    @Override
    public void readPeriodicInputs() {
        mPeriodicIO.beamBreakTriggered = mBeamBreak.get();
    }

    @Override
    public void writePeriodicOutputs() {
        // TODO Auto-generated method stub
        
        if(mSystemState == SystemState.IDLE)
        {
            mFinalStageMaster.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            mHopper.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            mFirstStageMaster.set(ControlMode.PercentOutput, mPeriodicIO.demand);
        }

        if(mSystemState == SystemState.SERIALIZE)
        {
            mFirstStageMaster.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            mHopper.set(ControlMode.PercentOutput, 0);
            mFinalStageMaster.set(ControlMode.PercentOutput, 0);
        }

        if (mSystemState == SystemState.REBALANCING)
        {
            mFirstStageMaster.set(ControlMode.PercentOutput, 0);
            mHopper.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            mFinalStageMaster.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            
        }
        
        if (mSystemState == SystemState.FEED)
        {
            if(mPeriodicIO.readyToShoot)
            {
                mFinalStageMaster.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            }
            mFirstStageMaster.set(ControlMode.PercentOutput, 0);
        }      
    }

    @Override
    public void onLoop(double timestamp) {
        switch(mWantedState)
        {
            case IDLE:
                mSystemState = SystemState.IDLE;
                break;
            case SERIALIZE:
                mSystemState = SystemState.SERIALIZE;
                break;
            case PREPARE_TO_SHOOT:
                mSystemState = SystemState.REBALANCING;
                break;
            case FEED:
                if(mPeriodicIO.readyToShoot && mSystemState == SystemState.REBALANCED)
                {
                    mSystemState = SystemState.FEED;
                }
                break;
            default:
                break;
        }

        switch(mSystemState)
        {
            case IDLE:
                mPeriodicIO.demand = 0;
                break;
            case SERIALIZE:
                mPeriodicIO.demand = kSerializeDemand;
                break;
            case REBALANCING:
                prepareToShoot(); 
            case REBALANCED:
                break;
            case FEED:
                mPeriodicIO.demand = kFeedDemand;
                break;
            default:
                break;
        }
    }

    @Override
    public void onDisableLoop() {
        // TODO Auto-generated method stub
        mFirstStageMaster.set(ControlMode.PercentOutput, 0);
        mHopper.set(ControlMode.PercentOutput, 0);
        mFinalStageMaster.set(ControlMode.PercentOutput, 0);
    }

    public void prepareToShoot()
    {
        if(mPeriodicIO.beamBreakTriggered)
        {
            mPeriodicIO.demand = 0d;
            mSystemState = SystemState.REBALANCED;
        }
        else
        {
            mPeriodicIO.demand = kPrepareDemand;
        }
          
    }

    public boolean getBeamBreakSensor()
    {
        return mPeriodicIO.beamBreakTriggered;
    }

    public void setReadyToShoot(boolean ready)
    {
        mPeriodicIO.readyToShoot = ready;
    }
}
