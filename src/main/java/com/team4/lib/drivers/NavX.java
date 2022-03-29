package com.team4.lib.drivers;

import com.kauailabs.navx.frc.AHRS;
import com.team254.lib.geometry.Rotation2d;

import edu.wpi.first.wpilibj.SPI;

/**
 * Driver for a NavX board. Basically a wrapper for the {@link AHRS} class
 */
public class NavX implements IMU {
    protected AHRS mAHRS;

    public NavX() {
        this(SPI.Port.kMXP);
    }

    public NavX(SPI.Port spi_port_id) {
        mAHRS = new AHRS(spi_port_id, (byte) 200);
    }

    @Override
    public boolean isPresent() {
        return mAHRS.isConnected();
    }

    @Override
    public synchronized boolean reset() {
        mAHRS.reset();
        return true;
    }

    public synchronized void zeroYaw() {
        mAHRS.zeroYaw();
    }

    @Override
    public double getHeadingDegrees() {
        return mAHRS.getAngle();
    }

    @Override
    public Rotation2d getHeading() {
        return Rotation2d.fromDegrees(getHeadingDegrees());
    }

}