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

    @Override
    public Path buildPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(0.0, 0.0, 0.0, 0.0));
        points.add(new Waypoint(11.0, 0.0, 5.0, 50.0));
        points.add(new Waypoint(28.3, -80.0, 5.0, 60.0));
        points.add(new Waypoint(90.0, -58.7, 15.0, 60.0, "Intake 1"));
        points.add(new Waypoint(80.0, 73.0, 0.0, 60.0, "Intake 2"));
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
