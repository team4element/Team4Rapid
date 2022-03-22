package com.team4.lib.drivers;

import com.team254.lib.geometry.Rotation2d;

public interface IMU {

	double getHeadingDegrees();

	Rotation2d getHeading();

	boolean isPresent();

	boolean reset();
}