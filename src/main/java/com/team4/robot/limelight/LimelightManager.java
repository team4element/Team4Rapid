package com.team4.robot.limelight;

import com.team4.robot.subsystems.Limelight;

public class LimelightManager {

    private Limelight shooterLimelight;

    private static LimelightManager mInstance;
    
    public static LimelightManager getInstance() {
        if (mInstance == null) {
            mInstance = new LimelightManager();
        }
        return mInstance;
    }


    private LimelightManager() {}

    public void setShooterLimelight(Limelight limelight) {
        shooterLimelight = limelight;
    }

    public Limelight getShooterLimelight() {
        return shooterLimelight;
    }

    public double getDistanceFromTarget()
    {
        return shooterLimelight.getDistance();
    }

    public void enableVision(boolean enable)
    {
        shooterLimelight.setVision(enable);
    }

    public void onLoop() {
        shooterLimelight.onLoop();
    }
}