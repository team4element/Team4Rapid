package com.team4.robot.controllers.utils;

/**
 * This helper class will return true once when the button is pressed.
 * It then waits for another press to be true again.
 */
public class OnRiseEventHelper {
	boolean lastValue = false;

	public boolean getAndUpdate(boolean newValue) {
		boolean isOnRise = !lastValue && newValue;

		lastValue = newValue;

		return isOnRise;
	}
}
