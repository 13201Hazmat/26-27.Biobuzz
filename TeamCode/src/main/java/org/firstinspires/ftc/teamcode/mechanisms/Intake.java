package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

/**
 * This section initiates an individual subsystem called Intake. This makes the
 * intake run forward, backwards, and turn off
 */
public class Intake implements Mechanism {
	private static final double FORWARD = 1.0;
	private static final double REVERSE = -1.0;
	private static final double OFF = 0.0;
	private static final double V_CHANGE_VAL = 0.1;

	// Power Value is current power of the intake, we set the power to 0 becasue the
	// intake starts off
	private double powerValue = 0;

	// This section helps us with config - combinging hardware and software
	// Defines the motors in module and port
	NextMotor intakeMotor = new NextMotor(Config.mModule, Config.mPort);
	NextServo intakeRampServo = new NextServo(Config.mModule, Config.mPort);
	private IntakeState intakeState;

	// Represents the state of the intake (forward, reverse, off, variable)
	public enum IntakeState {
		FORWARD, REVERSE, OFF, CALIB
	}
	private final double RAMP_UP_POS = 1.0;
	private final double RAMP_DOWN_POS = 0;

	// this section reverses the motor of the intake in case it is mounted backwards
	// setting the intake state to off so it won't move when you construct
	public Intake() {
		intakeMotor.setDirection(NextMotor.Direction.REVERSE);
		intakeState = IntakeState.OFF;
	}
	// this sections returns a command instantly and sets the intake to forward
	public Command setForward() {
		return instant(() -> setState(IntakeState.FORWARD));
	}
	// this sections returns a command instantly and sets the intake to off
	public Command setOff() {
		return instant(() -> setState(IntakeState.OFF));
	}
	// this section takes the current power value and decreases it by the
	// V_CHANGE_VAL (0.1)
	public Command downPower() {
		return instant(() -> {
			intakeState = IntakeState.CALIB;
			powerValue -= V_CHANGE_VAL;
			powerValue = Math.min(FORWARD, Math.max(powerValue, REVERSE));
		});
	}

	// this section takes the current power value and increases it by the
	// V_CHANGE_VAL (0.1)
	public Command upPower() {
		return instant(() -> {
			intakeState = IntakeState.CALIB;
			powerValue += V_CHANGE_VAL;
			powerValue = Math.min(FORWARD, Math.max(powerValue, REVERSE));
		});
	}
	// this sections returns a command instantly and sets the intake to reverse
	public Command setReverse() {
		return instant(() -> setState(IntakeState.REVERSE));
	}

	// this method returns the current speed
	// for debugging purposes
	public double getSpeed() {
		return intakeMotor.getThrottle();
	}
	// if the intake is going forward then we will press a button and it will
	// reverse
	// if the intake if going reverse then we will press a button and it will stop
	// if the intake is stopped we will press a button and it will go forward
	public void cycle() {
		if (intakeState == IntakeState.FORWARD) {
			intakeState = IntakeState.REVERSE;
		} else if (intakeState == IntakeState.REVERSE) {
			intakeState = IntakeState.OFF;
		} else {
			intakeState = IntakeState.FORWARD;
		}
	}

	public Command lowerIntakeRamp() {
		return instant(() -> intakeRampServo.setPosition(RAMP_DOWN_POS));
	}

	public Command raiseIntakeRamp() {
		return instant(() -> intakeRampServo.setPosition(RAMP_UP_POS));
	}

	public double getPosition() {
		return intakeRampServo.getPosition();
	}

	// this is a basic method to make the intake move forward, reverse, and off
	private void setState(IntakeState intakeState) {
		this.intakeState = intakeState;
	}

	// this returns the motor speed and the state (forward, reverse, and off) for
	// debugging
	public String debug() {
		return "INTAKE MOTOR SPEED: " + intakeMotor.getThrottle() + "\nINTAKE STATE: " + intakeState;
	}

	// periodic means the code will run for the entire teleOp
	// switch method is a better if statement
	// if forward, we will set powerValue to corresponding power
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
		// this actually sets the power
		intakeMotor.setThrottle(powerValue);
	}
}