package com.team4.robot.paths.threeball;

import java.util.ArrayList;
import java.util.List;

import com.team254.lib.control.Path;
import com.team254.lib.geometry.Pose2d;
import com.team4.lib.path.PathBuilder.Waypoint;
import com.team4.robot.Robot;
import com.team4.lib.path.PathBuilder;
import com.team4.lib.path.PathContainer;

public class ReturnPath implements PathContainer {

    @Override
    public Path buildPath() {
        List<Waypoint> points = new ArrayList<>();
        points.add(new Waypoint(80.0, 73.0, 0.0, 0.0)); // Point E
        points.add(new Waypoint(45.0, 10.0, 5.0, 50.0));
        points.add(new Waypoint(-5.0, -5.0, 0.0, 60.0));
        return PathBuilder.buildPathFromWaypoints(points);
    }

    @Override
    public Pose2d getStartPose() {
        return new Pose2d(80.0, 73.0, Robot.mDrive.getAngle()); // Point E
    }

    @Override
    public boolean isReversed() {
        return true;
    }
    
}
