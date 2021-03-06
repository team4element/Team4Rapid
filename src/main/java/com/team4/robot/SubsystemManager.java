package com.team4.robot;

import java.util.Arrays;
import java.util.List;

import com.team4.robot.subsystems.Subsystem;

import edu.wpi.first.wpilibj.Timer;

/**
 * This is a class that manages all the subsystems.
 * Usually the operation of a subsystem works in the same way.
 * Check the `onLoop` method of each subsystem to see how it works.
 * 
 */
public class SubsystemManager {
    public static SubsystemManager mInstance = null;
    private List<Subsystem> mAllSubsystems;

    public static SubsystemManager getInstance() {
        if (mInstance == null) {
            mInstance = new SubsystemManager();
        }
        return mInstance;
    }

    public void setSubsystems(Subsystem... allSubsystems) {
        mAllSubsystems = Arrays.asList(allSubsystems);
    }

    public void onEnabledLoop() {
        mAllSubsystems.forEach(Subsystem::readPeriodicInputs);
        mAllSubsystems.forEach(l -> l.onLoop(Timer.getFPGATimestamp()));
        mAllSubsystems.forEach(Subsystem::writePeriodicOutputs);
    }

    public void onDisabledLoop() {
        mAllSubsystems.forEach(Subsystem::readPeriodicInputs);
        mAllSubsystems.forEach(Subsystem::onDisableLoop);
    }

    public void onDisabledStop() {}

    public void onEnabledStop() {}

    public void onEnabledStart() {
			mAllSubsystems.forEach(Subsystem::onEnabledStart);
		}

    public void onDisabledStart() {}

    public void onSimulationLoop() {
        mAllSubsystems.forEach(Subsystem::onSimulationLoop);
    }
}
