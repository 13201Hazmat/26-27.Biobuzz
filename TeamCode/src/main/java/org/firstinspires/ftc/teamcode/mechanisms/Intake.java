package org.firstinspires.ftc.teamcode.mechanisms;

import static com.pedropathing.ivy.commands.Commands.conditional;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.telemetry.Telemetry;

public class Intake implements Mechanism {

	public enum IntakeState {
		FORWARD, REVERSE, OFF, CALIB
	}

	private static final double FORWARD = 1.0;
	private static final double REVERSE = -1.0;
	private static final double OFF = 0.0;
	private static final double CALIB_DELTA = 0.1;

	private final double RAMP_UP_POS = 1.0;
	private final double RAMP_DOWN_POS = 0.0;

	private double powerValue = 0.0;

	private NextMotor intakeMotor = new NextMotor(Config.intakeMotorModule, Config.intakeMotorPort);
	private NextServo leftIntakeRampServo = new NextServo(
		Config.leftIntakeRampServoModule, Config.leftIntakeRampServoPort, 0.0
	);
	private NextServo rightIntakeRampServo = new NextServo(
		Config.rightIntakeRampServoModule, Config.rightIntakeRampServoPort, 0.0
	);
	private IntakeState intakeState;

	public Intake() {
		intakeMotor.setDirection(NextMotor.Direction.REVERSE);
		intakeState = IntakeState.OFF;
		rightIntakeRampServo.setDirection(NextMotor.Direction.REVERSE);
	}

	public double getPosition() {
		return leftIntakeRampServo.getPosition();
	}

	public double getSpeed() {
		return intakeMotor.getThrottle();
	}

	private void setState(IntakeState intakeState) {
		this.intakeState = intakeState;
	}

	public void forward() {
		setState(IntakeState.FORWARD);
	}

	public void reverse() {
		setState(IntakeState.REVERSE);
	}

	public void off() {
		setState(IntakeState.OFF);
	}

	public void setRampPosition(double pos) {
		rightIntakeRampServo.setPosition(pos);
		leftIntakeRampServo.setPosition(pos);
	}

	public Command setForward() {
		return instant(this::forward);
	}

	public Command setReverse() {
		return instant(this::reverse);
	}

	public Command setOff() {
		return instant(this::off);
	}

	public Command setRampUp() {
		return instant(() -> setRampPosition(RAMP_UP_POS));
	}

	public Command setRampDown() {
		return instant(() -> setRampPosition(RAMP_DOWN_POS));
	}

	public Command toggleRamp() {
		return conditional(
			() -> leftIntakeRampServo.getPosition() == RAMP_UP_POS,
			setRampDown(),
			setRampUp()
		);
	}

	public Command increment(double i) {
		return instant(() -> setRampPosition(Math.min(1.0, Math.max(0.0, getPosition() + i))));
	}

	public Command incrementRamp() {
		return instant(() -> setRampPosition(getPosition() + 0.01));
	}

	public Command decrementRamp() {
		return instant(() -> setRampPosition(getPosition() - 0.01));
	}

	public Command increasePower() {
		return instant(() -> {
			intakeState = IntakeState.CALIB;
			powerValue += CALIB_DELTA;
			powerValue = Math.min(FORWARD, Math.max(powerValue, REVERSE));
		});
	}

	public Command decreasePower() {
		return instant(() -> {
			intakeState = IntakeState.CALIB;
			powerValue -= CALIB_DELTA;
			powerValue = Math.min(FORWARD, Math.max(powerValue, REVERSE));
		});
	}

	public void cycle() {
		if (intakeState == IntakeState.FORWARD) {
			intakeState = IntakeState.REVERSE;
		} else if (intakeState == IntakeState.REVERSE) {
			intakeState = IntakeState.OFF;
		} else {
			intakeState = IntakeState.FORWARD;
		}
	}

	public void debug() {
		Telemetry.log("INTAKE STATE", intakeState);
		Telemetry.log("INTAKE MOTOR SPEED:", intakeMotor.getThrottle());
		Telemetry.log("RAMP POSITION", leftIntakeRampServo.getPosition());
	}

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