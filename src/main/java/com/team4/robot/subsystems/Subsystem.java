package com.team4.robot.subsystems;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

// Currently extending SubsystemBase because it handles adding the subsystem to the Scheduler.
// Team 254 manages their own scheduler using a Notifier.
// We will only add this complexity later if we really want it
public abstract class Subsystem extends SubsystemBase {
    // This is used to aggregate all reads in a single call
    public void readPeriodicInputs() {}
    // This is used to aggregate all writes in a single call
    public void writePeriodicOutputs() {}

    // This is called in between the Input and Output calls
    public void onLoop() {}
    
    public void onSimulationLoop() {}
}
