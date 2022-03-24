package com.team4.robot.paths;

import java.util.ArrayList;
import java.util.List;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Rotation2d;
import com.team4.lib.trajectory.SimpleTrajectory;
import com.team4.lib.trajectory.Waypoint;

public class TestStraightPath extends SimpleTrajectory {

    @Override
    public Waypoint points() {
        List<Pose2d> points = new ArrayList<>();
        points.add(new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(0.0)));
        points.add(new Pose2d(80.0, 0.0, Rotation2d.fromDegrees(0.0)));
        points.add(new Pose2d(160.0, 0.0, Rotation2d.fromDegrees(0.0)));        

        return Waypoint.createWaypointFromPoints(false, points);
    }

    @Override
    public Pose2d startPose() {
        return new Pose2d(0.0, 0.0, Rotation2d.fromDegrees(0.0));
    }
    
}
