package com.team4.lib.util;

import java.util.Map;

import com.team254.lib.geometry.Pose2d;
import com.team254.lib.geometry.Twist2d;
import com.team254.lib.util.InterpolatingDouble;
import com.team254.lib.util.InterpolatingTreeMap;
import com.team254.lib.util.MovingAverageTwist2d;

import edu.wpi.first.wpilibj.Timer;

public class FieldState {

	private final int kObservationBufferSize = 100;

    private InterpolatingTreeMap<InterpolatingDouble, Pose2d> mFieldToVehicle;
    private Twist2d mPredictedVelocity;
    private Twist2d mMeasuredVelocity;
    private MovingAverageTwist2d mFilteredVelocity;
    private double mDistanceDriven;

    

    public FieldState()
    {
        reset(0.0, Pose2d.identity());
    }

    public synchronized void reset(double startTime, Pose2d initialPosition) {
        mFieldToVehicle = new InterpolatingTreeMap<>(kObservationBufferSize);
        mFieldToVehicle.put(new InterpolatingDouble(startTime), initialPosition);
        mPredictedVelocity = Twist2d.identity();
        mMeasuredVelocity = Twist2d.identity();
        mFilteredVelocity = new MovingAverageTwist2d(25);
        resetDistanceDriven();
    }

    public synchronized void reset() {
        reset(Timer.getFPGATimestamp(), Pose2d.identity());
    }

    public synchronized Pose2d getFieldToVehicle(double timestamp) {
        return mFieldToVehicle.getInterpolated(new InterpolatingDouble(timestamp));
    }

    public synchronized Map.Entry<InterpolatingDouble, Pose2d> getLatestFieldToVehicle() {
        return mFieldToVehicle.lastEntry();
    }

    public synchronized Pose2d getPredictedFieldToVehicle(double lookahead_time) {
        return getLatestFieldToVehicle().getValue()
                .transformBy(Pose2d.exp(mPredictedVelocity.scaled(lookahead_time)));
    }

    public synchronized void addFieldToVehicleObservation(double timestamp, Pose2d observation) {
        mFieldToVehicle.put(new InterpolatingDouble(timestamp), observation);
    }

    public synchronized void addObservations(double timestamp, Twist2d displacement, Twist2d measured_velocity,
                                             Twist2d predictedVelocity) {
        mDistanceDriven += displacement.dx;
        addFieldToVehicleObservation(timestamp, 
            Kinematics.integrateForwardKinematics(getLatestFieldToVehicle().getValue(), 
            displacement));

        mMeasuredVelocity = measured_velocity;
        
        if (Math.abs(mMeasuredVelocity.dtheta) < 2.0 * Math.PI) 
        {
            // Reject really high angular velocities from the filter.
            mFilteredVelocity.add(mMeasuredVelocity);
        } 
        else 
        {
            mFilteredVelocity.add(new Twist2d(mMeasuredVelocity.dx, mMeasuredVelocity.dy, 0.0));
        }
        
        mPredictedVelocity = predictedVelocity;
    }

    public synchronized double getDistanceDriven() {
        return mDistanceDriven;
    }

    public synchronized void resetDistanceDriven() {
        mDistanceDriven = 0.0;
    }

    public synchronized Twist2d getPredictedVelocity() {
        return mPredictedVelocity;
    }

    public synchronized Twist2d getMeasuredVelocity() {
        return mMeasuredVelocity;
    }

    public synchronized Twist2d getSmoothedVelocity() {
        return mFilteredVelocity.getAverage();
    }
}
