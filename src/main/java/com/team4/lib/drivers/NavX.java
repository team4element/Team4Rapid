package com.team4.lib.drivers;

import com.kauailabs.navx.AHRSProtocol.AHRSUpdateBase;
import com.kauailabs.navx.frc.AHRS;
import com.kauailabs.navx.frc.ITimestampedDataSubscriber;
import com.team254.lib.geometry.Rotation2d;

import edu.wpi.first.wpilibj.SPI;

/**
 * Driver for a NavX board. Basically a wrapper for the {@link AHRS} class
 */
public class NavX implements IMU {
    protected class Callback implements ITimestampedDataSubscriber {
        @Override
        public void timestampedDataReceived(long system_timestamp, long sensor_timestamp, AHRSUpdateBase update,
                                            Object context) {
            synchronized (NavX.this) {
                // This handles the fact that the sensor is inverted from our coordinate conventions.
                mLastSensorTimestampMs = sensor_timestamp;
                mHeadingDegrees = -update.fused_heading;
            }
        }
    }

    protected AHRS mAHRS;

    protected double mHeadingDegrees;
    protected final long kInvalidTimestamp = -1;
    protected long mLastSensorTimestampMs;


    public NavX() {
        this(SPI.Port.kMXP);
    }

    public NavX(SPI.Port spi_port_id) {
        mAHRS = new AHRS(spi_port_id, (byte) 200);
        resetState();
        mAHRS.registerCallback(new Callback(), null);
    }

    @Override
    public boolean isPresent() {
        return mAHRS.isConnected();
    }

    @Override
    public synchronized boolean reset() {
        mAHRS.reset();
        resetState();
        return true;
    }

    public synchronized void zeroYaw() {
        mAHRS.zeroYaw();
        resetState();
    }

    private void resetState() {
        mLastSensorTimestampMs = kInvalidTimestamp;
    }

    @Override
    public double getHeadingDegrees() {
        return mHeadingDegrees;
    }

    @Override
    public Rotation2d getHeading() {
        return Rotation2d.fromDegrees(mHeadingDegrees);
    }

}