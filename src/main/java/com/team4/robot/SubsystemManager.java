package com.team4.robot;

import java.util.Arrays;
import java.util.List;

import com.team4.robot.subsystems.Subsystem;

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

    /**
     * This represents the generalized way that a Subsystem should operate.
     * 1. Read all sensor inputs
     * 2. Run the subsystem's calculations
     * 3. Write all outputs to the actuators
     */
    public void onEnabledLoop() {
        mAllSubsystems.forEach(Subsystem::readPeriodicInputs);
        mAllSubsystems.forEach(Subsystem::onLoop);
        mAllSubsystems.forEach(Subsystem::writePeriodicOutputs);
    }

    public void onDisabledLoop() {
        mAllSubsystems.forEach(Subsystem::readPeriodicInputs);
    }

    public void onDisabledStop() {}

    public void onEnabledStop() {}
}
