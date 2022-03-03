// package com.team4.robot.subsystems;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.NeutralMode;
// import com.ctre.phoenix.motorcontrol.can.WPI_TalonFX;
// import com.fasterxml.jackson.annotation.JsonKey;
// import com.team4.lib.drivers.TalonFactory;
// import com.team4.robot.Constants;

// import edu.wpi.first.wpilibj.DigitalInput;
// import io.github.oblarg.oblog.Loggable;
// import io.github.oblarg.oblog.annotations.Log;

// // class ConveyorPeriodicIO implements Loggable
// // {
// //     @Log
// // 	public double demand;
// //     @Log
// //     public boolean readyToShoot;
// //     @Log
// //     public boolean beamBreakTriggered;
// //     public boolean isStowed;

// // }

// public class Conveyor extends Subsystem<ConveyorPeriodicIO> {
//     private static Conveyor mInstance = null;

//     @Log
//     private WantedState mWantedState = WantedState.IDLE;
    
//     @Log
//     private SystemState mSystemState = SystemState.IDLE;

//     private final double kSerializeDemand = .75;
//     private final double kPrepareDemand = .5;
//     private final double kFeedDemand = .75;

//     //TODO: Change to actual motor controllers
//     private final WPI_TalonFX mIntakeFirst;//, mFirstStageSlave;
//     private final WPI_TalonFX mConveyor;
//     private final WPI_TalonFX mIntakeLast;//, mFinalStageSlave; 

//     // private final DigitalInput mBeamBreak;

//     public enum WantedState {
//         IDLE,
//         SERIALIZE,
//         PREPARE_TO_SHOOT,
//         FEED
//     }

    
//     public enum SystemState {
//         IDLE,
//         SERIALIZE,
//         FEED,
//         REBALANCING,
//         REBALANCED,
//     }

//     private Conveyor()
//     {
//         // TODO: Add constants
//         mIntakeFirst = TalonFactory.createDefaultTalonFX(Constants.kIntakeFirst);
//         //mFirstStageSlave = TalonFactory.createPermanentSlaveTalonFX(Constants.kConveyorFirstStageLeft, mIntakeFirst);
        
//         mConveyor = TalonFactory.createDefaultTalonFX(Constants.kConveyor);
        
//         mIntakeLast = TalonFactory.createDefaultTalonFX(Constants.kIntakeLast);
//         //mFinalStageSlave = TalonFactory.createPermanentSlaveTalonFX(Constants.kConveyorFinalStageBottom, mIntakeLast);

//         mIntakeFirst.setNeutralMode(NeutralMode.Coast);
//         mConveyor.setNeutralMode(NeutralMode.Coast);
//         mIntakeLast.setNeutralMode(NeutralMode.Coast);
        
//         // mBeamBreak = new DigitalInput(0);

//     }

//     public static Conveyor getInstance()
//     {
//         if(mInstance == null)
//         {
//             mInstance = new Conveyor();
//         }

//         return mInstance;
//     }

//     @Override
//     public void readPeriodicInputs() {
//         // mPeriodicIO.beamBreakTriggered = mBeamBreak.get();
//     }

//     @Override
//     public void writePeriodicOutputs() {
//         // TODO Auto-generated method stub
        
//         if(mSystemState == SystemState.IDLE)
//         {
//             mIntakeLast.set(ControlMode.PercentOutput, mPeriodicIO.demand);
//             mConveyor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
//             mIntakeFirst.set(ControlMode.PercentOutput, mPeriodicIO.demand);
//         }

//         if(mSystemState == SystemState.SERIALIZE)
//         {
//             mIntakeFirst.set(ControlMode.PercentOutput, mPeriodicIO.demand);
//             mConveyor.set(ControlMode.PercentOutput, 0);
//             mIntakeLast.set(ControlMode.PercentOutput, 0);
//         }

//         if (mSystemState == SystemState.REBALANCING)
//         {
//             mIntakeFirst.set(ControlMode.PercentOutput, 0);
//             mConveyor.set(ControlMode.PercentOutput, mPeriodicIO.demand);
//             mIntakeLast.set(ControlMode.PercentOutput, mPeriodicIO.demand);
            
//         }
        
//         if (mSystemState == SystemState.FEED)
//         {
//             if(mPeriodicIO.readyToShoot)
//             {
//                 mIntakeLast.set(ControlMode.PercentOutput, mPeriodicIO.demand);
//             }
//             mIntakeFirst.set(ControlMode.PercentOutput, 0);
//         }      
//     }

//     @Override
//     public void onLoop(double timestamp) {
//         switch(mWantedState)
//         {
//             case IDLE:
//                 mSystemState = SystemState.IDLE;
//                 break;
//             case SERIALIZE:
//                 mSystemState = SystemState.SERIALIZE;
//                 break;
//             case PREPARE_TO_SHOOT:
//                 mSystemState = SystemState.REBALANCING;
//                 break;
//             case FEED:
//                 if(mPeriodicIO.readyToShoot && mSystemState == SystemState.REBALANCED)
//                 {
//                     mSystemState = SystemState.FEED;
//                 }
//                 break;
//             default:
//                 break;
//         }

//         switch(mSystemState)
//         {
//             case IDLE:
//                 mPeriodicIO.demand = 0;
//                 break;
//             case SERIALIZE:
//                 mPeriodicIO.demand = kSerializeDemand;
//                 break;
//             case REBALANCING:
//                 prepareToShoot(); 
//             case REBALANCED:
//                 break;
//             case FEED:
//                 mPeriodicIO.demand = kFeedDemand;
//                 break;
//             default:
//                 break;
//         }
//     }

//     @Override
//     public void onDisableLoop() {
//         // TODO Auto-generated method stub
//         mIntakeFirst.set(ControlMode.PercentOutput, 0);
//         mConveyor.set(ControlMode.PercentOutput, 0);
//         mIntakeLast.set(ControlMode.PercentOutput, 0);
//     }

//     public void prepareToShoot()
//     {
//         if(mPeriodicIO.beamBreakTriggered)
//         {
//             mPeriodicIO.demand = 0d;
//             mSystemState = SystemState.REBALANCED;
//         }
//         else
//         {
//             mPeriodicIO.demand = kPrepareDemand;
//         }
          
//     }

//     public boolean getBeamBreakSensor()
//     {
//         return mPeriodicIO.beamBreakTriggered;
//     }

//     public void setReadyToShoot(boolean ready)
//     {
//         mPeriodicIO.readyToShoot = ready;
//     }

//     public void setWantedState(com.team4.robot.subsystems.Intake.WantedState idle) {
//     }
// }
