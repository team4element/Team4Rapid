package com.team4.lib.util;

import com.team254.lib.util.DriveSignal;

public class DriveSpeed {
    double mLeftVelocity;
    double mRightVelocity;

    double mLeftFeedForward;
    double mRightFeedForward;
    
    public DriveSpeed(double leftVel, double rightVel, double leftFF, double rightFF)
    {
        mLeftVelocity = leftVel;
        mRightVelocity = rightVel;

        mLeftFeedForward = leftFF;
        mRightFeedForward = rightFF;
    }

    public DriveSignal getVelocity()
    {
        return new DriveSignal(mLeftVelocity, mRightVelocity);
    }

    public DriveSignal getFeedForward()
    {
        return new DriveSignal(mLeftFeedForward, mRightFeedForward);
    }
}
