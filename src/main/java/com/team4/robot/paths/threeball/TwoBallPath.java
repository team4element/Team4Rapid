package com.team4.robot.paths.threeball;

import java.util.ArrayList;
import java.util.List;

import com.team254.lib.control.Path;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team4.lib.path.PathBuilder;
import com.team4.lib.path.PathBuilder.Waypoint;
import com.team4.lib.path.PathContainer;

public class TwoBallPath implements PathContainer {

    /**
     * When modifying based off comp field only change the first 2 values of each Waypoint 
     * 3rd and 4th need to stay the same for the path to work
     */

    @Override
    public Path buildPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(0.0, 0.0, 0.0, 0.0)); // Point A
        points.add(new Waypoint(11.0, 0.0, 5.0, 50.0)); // Point B
        points.add(new Waypoint(18.3, -65.0, 5.0, 60.0)); // Point C
        points.add(new Waypoint(75.0, -58.7, 12.5, 60.0, "Intake One")); // Point D
        points.add(new Waypoint(75.0, 76.0, 0.0, 60.0)); // Point E
        return PathBuilder.buildPathFromWaypoints(points);
    }

    @Override
    public Pose2d getStartPose() {
        return new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(180.0));
    }

    @Override
    public boolean isReversed() {
        return false;
    }
    
}
