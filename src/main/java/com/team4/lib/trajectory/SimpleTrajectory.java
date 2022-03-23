package com.team4.lib.trajectory;

import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.physics.DifferentialDrive;
import com.team254.lib.trajectory.Trajectory;
import com.team254.lib.trajectory.timing.TimedState;

public abstract class SimpleTrajectory {
    
    public abstract Waypoint points();

    public Trajectory<TimedState<Pose2dWithCurvature>> generateTrajectory(DifferentialDrive model)
    {
        return TrajectoryGenerator.generateTrajectory(points(), model); 
    }
}
