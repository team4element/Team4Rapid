package frc.robot.subsystems;

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

import edu.wpi.first.math.Nat;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.controller.LinearQuadraticRegulator;
import edu.wpi.first.math.estimator.KalmanFilter;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.system.LinearSystem;
import edu.wpi.first.math.system.LinearSystemLoop;
import edu.wpi.first.math.system.plant.DCMotor;
import edu.wpi.first.math.system.plant.LinearSystemId;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.RobotController;
import edu.wpi.first.wpilibj.motorcontrol.MotorController;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import edu.wpi.first.wpilibj.simulation.BatterySim;
import edu.wpi.first.wpilibj.simulation.EncoderSim;
import edu.wpi.first.wpilibj.simulation.FlywheelSim;
import edu.wpi.first.wpilibj.simulation.RoboRioSim;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * This is a sample program to demonstrate how to use a state-space controller
 * to control a
 * flywheel.
 * Based off of FlywheelSim
 */
public class Shooter {
    private static final int kMotorPort = 0;
    private static final int kEncoderAChannel = 0;
    private static final int kEncoderBChannel = 1;

    private static final double kFlywheelMomentOfInertia = 0.003440853333;

    // > 1.0 means flywheel geared down
    private static final double kFlywheelGearing = 1.0;

    // The plant holds a state-space model of our flywheel. This system has the
    // following properties:
    //
    // States: [velocity], in radians per second.
    // Inputs (what we can "put in"): [voltage], in volts.
    // Outputs (what we can measure): [velocity], in radians per second.
    private final LinearSystem<N1, N1, N1> m_flywheelPlant = LinearSystemId.createFlywheelSystem(
            DCMotor.getFalcon500(1), kFlywheelMomentOfInertia, kFlywheelGearing);

    // The observer fuses our encoder data and voltage inputs to reject noise.
    private final KalmanFilter<N1, N1, N1> m_observer = new KalmanFilter<>(
            Nat.N1(),
            Nat.N1(),
            m_flywheelPlant,
            VecBuilder.fill(3.0), // How accurate we think our model is
            VecBuilder.fill(0.01), // How accurate we think our encoder
            // data is
            0.020);

    // A LQR uses feedback to create voltage commands.
    private final LinearQuadraticRegulator<N1, N1, N1> m_controller = new LinearQuadraticRegulator<>(
            m_flywheelPlant,
            VecBuilder.fill(8.0), // qelms. Velocity error tolerance, in radians per second. Decrease
            // this to more heavily penalize state excursion, or make the controller behave
            // more
            // aggressively.
            VecBuilder.fill(12.0), // relms. Control effort (voltage) tolerance. Decrease this to more
            // heavily penalize control effort, or make the controller less aggressive. 12
            // is a good
            // starting point because that is the (approximate) maximum voltage of a
            // battery.
            0.020); // Nominal time between loops. 0.020 for TimedRobot, but can be
    // lower if using notifiers.

    // The state-space loop combines a controller, observer, feedforward and plant
    // for easy control.
    private final LinearSystemLoop<N1, N1, N1> m_loop = new LinearSystemLoop<>(m_flywheelPlant, m_controller,
            m_observer, 12.0, 0.020);

    private final FlywheelSim m_FlywheelSim = new FlywheelSim(m_flywheelPlant, DCMotor.getFalcon500(1),
            kFlywheelGearing);
    // An encoder set up to measure flywheel velocity in radians per second.
    private final Encoder m_encoder = new Encoder(kEncoderAChannel, kEncoderBChannel);
    private final EncoderSim m_encoderSim = new EncoderSim(m_encoder);

    private final MotorController m_motor = new PWMSparkMax(kMotorPort);

    // A joystick to read the trigger from.

    public Shooter() {
        // We go 2 pi radians per 4096 clicks.
        m_encoder.setDistancePerPulse(2.0 * Math.PI / 4096.0);

    }

    public void zeroControlLoop() {
        m_loop.reset(VecBuilder.fill(m_encoder.getRate()));
    }

    public void setToSetpoint(double setpointRadPerSec) {
        m_loop.setNextR(VecBuilder.fill(setpointRadPerSec));

        // Correct our Kalman filter's state vector estimate with encoder data.
        m_loop.correct(VecBuilder.fill(m_encoder.getRate()));

        // Update our LQR to generate new voltage commands and use the voltages to
        // predict the next
        // state with out Kalman filter.
        m_loop.predict(0.020);

        // Send the new calculated voltage to the motors.
        // voltage = duty cycle * battery voltage, so
        // duty cycle = voltage / battery voltage
        double nextVoltage = m_loop.getU(0);
        m_motor.setVoltage(nextVoltage);
    }

    public void onSimulation() {
        m_FlywheelSim.setInput(m_motor.get() * RobotController.getBatteryVoltage());
        m_FlywheelSim.update(0.020);
    
        // TODO: How do I update the m_encoderSim's state?
        m_encoderSim.setRate(m_FlywheelSim.getAngularVelocityRadPerSec());
    
        SmartDashboard.putNumber("RPM", Units.radiansPerSecondToRotationsPerMinute(m_encoderSim.getRate()));
        RoboRioSim.setVInVoltage(BatterySim.calculateDefaultBatteryLoadedVoltage(m_FlywheelSim.getCurrentDrawAmps()));
    }
}
