package com.team4.robot.limelight.constants;

import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;

public class LimelightConstants {
    private String name;
    private String tableName;

    private double height;
    private double target;
    private double angle;

    public enum Type {
        Shooter
    }

    public LimelightConstants(String name, String tableName, double height, double target, double angle) {
        this.name = name;
        this.tableName = tableName;
        this.height = height;
        this.target = target;
        this.angle = angle;
    }

    public String getName() {
        return name;
    }

    public String getTableName() {
        return tableName;
    }

    public double getHeight()
    {
        return height;
    }

    public double getTarget()
    {
        return target;
    }

    public double getAngle()
    {
        return angle;
    }

}