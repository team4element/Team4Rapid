package com.team4.robot.paths;

import java.util.ArrayList;
import java.util.List;

import com.team254.lib.control.Path;
import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team4.lib.path.PathBuilder;
import com.team4.lib.path.PathBuilder.Waypoint;
import com.team4.lib.path.PathContainer;

public class TestCurvePath implements PathContainer {
    @Override
    public Path buildPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(50, 0, 0, 0)); //Change X to 395
        points.add(new Waypoint(90, 0, 15, 10)); // Change X to 370
        points.add(new Waypoint(120, 30, 15, 20));
        points.add(new Waypoint(160, 50, 0, 20));
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
