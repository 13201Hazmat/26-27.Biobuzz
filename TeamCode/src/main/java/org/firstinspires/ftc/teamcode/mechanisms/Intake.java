package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Intake implements Mechanism {
	private static final double FORWARD = 1.0;
	private static final double REVERSE = -1.0;
	private static final double OFF = 0.0;
	private static final double CALIB_DELTA = 0.1;

	private final double RAMP_UP_POS = 1.0;
	private final double RAMP_DOWN_POS = 0;

	private double powerValue = 0;

	NextMotor intakeMotor = new NextMotor(Config.mModule, Config.mPort);
	NextServo intakeRampServo = new NextServo(Config.mModule, Config.mPort);
	private IntakeState intakeState;

	public enum IntakeState {
		FORWARD, REVERSE, OFF, CALIB
	}

	/**
	 * Constructs the Intake subsystem, sets the motor direction to reversed, and
	 * initializes the state to OFF.
	 */
	public Intake() {
		intakeMotor.setDirection(NextMotor.Direction.REVERSE);
		intakeState = IntakeState.OFF;
	}

	/**
	 * Gets the current position of the intake ramp servo.
	 *
	 * @return the servo position as a double
	 */
	public double getPosition() {
		return intakeRampServo.getPosition();
	}

	/**
	 * Gets the current throttle speed of the intake motor.
	 *
	 * @return the current motor throttle value
	 */
	public double getSpeed() {
		return intakeMotor.getThrottle();
	}

	/**
	 * Helper method to set the internal state of the intake.
	 *
	 * @param intakeState
	 *            the new IntakeState to apply
	 */
	private void setState(IntakeState intakeState) {
		this.intakeState = intakeState;
	}

	/**
	 * Sets the intake state to FORWARD.
	 */
	public void forward() {
		setState(IntakeState.FORWARD);
	}

	/**
	 * Sets the intake state to OFF.
	 */
	public void off() {
		setState(IntakeState.OFF);
	}

	/**
	 * Sets the intake state to REVERSE.
	 */
	public void reverse() {
		setState(IntakeState.REVERSE);
	}

	/**
	 * Sets the intake state to FORWARD.
	 *
	 * @return an instant Command to set the state to FORWARD
	 */
	public Command setForward() {
		return instant(this::forward);
	}

	/**
	 * Sets the intake state to OFF.
	 *
	 * @return an instant Command to set the state to OFF
	 */
	public Command setOff() {
		return instant(this::off);
	}

	/**
	 * Sets the intake state to REVERSE.
	 *
	 * @return an instant Command to set the state to REVERSE
	 */
	public Command setReverse() {
		return instant(this::reverse);
	}
	/**
	 * Moves the ramp servo up to its raised position.
	 *
	 * @return an instant Command to raise the ramp servo
	 */
	public Command setRampUp() {
		return instant(() -> intakeRampServo.setPosition(RAMP_UP_POS));
	}

	/**
	 * Moves the ramp servo down to its lowered position.
	 *
	 * @return an instant Command to lower the ramp servo
	 */
	public Command setRampDown() {
		return instant(() -> intakeRampServo.setPosition(RAMP_DOWN_POS));
	}

	/**
	 * Cycles through intake states in sequence: FORWARD -> REVERSE -> OFF ->
	 * FORWARD.
	 */
	public void cycle() {
		if (intakeState == IntakeState.FORWARD) {
			intakeState = IntakeState.REVERSE;
		} else if (intakeState == IntakeState.REVERSE) {
			intakeState = IntakeState.OFF;
		} else {
			intakeState = IntakeState.FORWARD;
		}
	}

	/**
	 * Increases the intake power by the calibration delta value (0.1) and clamps it
	 * between REVERSE and FORWARD.
	 *
	 * @return an instant Command to increase power
	 */
	public Command increasePower() {
		return instant(() -> {
			intakeState = IntakeState.CALIB;
			powerValue += CALIB_DELTA;
			powerValue = Math.min(FORWARD, Math.max(powerValue, REVERSE));
		});
	}

	/**
	 * Decreases the intake power by the calibration delta value (0.1) and clamps it
	 * between REVERSE and FORWARD.
	 *
	 * @return an instant Command to decrease power
	 */
	public Command decreasePower() {
		return instant(() -> {
			intakeState = IntakeState.CALIB;
			powerValue -= CALIB_DELTA;
			powerValue = Math.min(FORWARD, Math.max(powerValue, REVERSE));
		});
	}

	/**
	 * Logs the current motor speed and intake state to telemetry for debugging
	 * purposes.
	 */
	public void debug() {
		Telemetry.log("INTAKE STATE", intakeState);
		Telemetry.log("INTAKE MOTOR SPEED:", intakeMotor.getThrottle());
		Telemetry.log("RAMP POSITION", intakeRampServo.getPosition());
	}

	/**
	 * Periodic method that runs repeatedly during TeleOp. Updates the power value
	 * based on the current intake state and applies it to the motor throttle.
	 */
	@Override
	public void periodic() {
		switch (intakeState) {
			case FORWARD :
				powerValue = FORWARD;
				break;
			case REVERSE :
				powerValue = REVERSE;
				break;
			case OFF :
				powerValue = OFF;
				break;
		}
		intakeMotor.setThrottle(powerValue);
	}
}