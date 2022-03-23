package com.team4.lib.trajectory;

import com.team254.lib.geometry.Pose2d;
import com.team4.lib.util.DriveSpeed;

public class DrivePathPlanner {
    public DrivePathPlanner()
    {

    }

    public DriveSpeed update(double timestamp, Pose2d state)
    {
        return new DriveSpeed(0, 0, 0, 0);
    }

}
