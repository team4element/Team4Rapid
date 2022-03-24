package com.team4.lib.trajectory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Pose2dWithCurvature;
import com.team254.lib.trajectory.timing.TimingConstraint;

public class Waypoint {

    boolean isReversed = false;
    List<Pose2d> pointList;
    List<TimingConstraint<Pose2dWithCurvature>> constraints;
    double startVel;
    double endVel;

    public Waypoint()
    {
        this(false, new ArrayList<Pose2d>(), new ArrayList<TimingConstraint<Pose2dWithCurvature>>(), 0.0, 0.0);
    }

    public Waypoint(boolean reversed, 
                    List<Pose2d> pointList, 
                    List<TimingConstraint<Pose2dWithCurvature>> constraints,
                    double startVel,
                    double endVel
                    )
    {
        this.isReversed = reversed;
        this.pointList = pointList;
        this.constraints = constraints;
        this.startVel = startVel;
        this.endVel = endVel;
    }

    public Waypoint flip(Waypoint waypoint, Pose2d flipFactor)
    {
        Waypoint newPoints = new Waypoint();
        for (int i = 0; i < waypoint.pointList.size(); i++) {
            newPoints.pointList.add(waypoint.pointList.get(i).transformBy(flipFactor));
        }

        return newPoints;
    }

    public static Waypoint createWaypointFromPoints(boolean reversed, List<Pose2d> points)
    {
        return new Waypoint(reversed, points, Arrays.asList(), 0.0, 0.0);
    }
}
