package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class Intake implements Mechanism {
	private static final double FORWARD = 1.0;
	private static final double REVERSE = -1.0;
	private static final double OFF = 0.0;

	private static final double RAMP_UP_POS = 1.0;
	private static final double RAMP_DOWN_POS = 0.0;

	private final NextMotor intakeMotor = new NextMotor(Config.mModule, Config.mPort);
	private final NextServo intakeRampServo = new NextServo(Config.jModule, Config.jPort);
	private IntakeState intakeState;

	public enum IntakeState {
		FORWARD, REVERSE, OFF
	}

	public Intake() {
		intakeMotor.setDirection(NextMotor.Direction.REVERSE);
		intakeState = IntakeState.OFF;
	}

	public Command moveRamp(double pos) {
		return instant(() -> intakeRampServo.setPosition(pos));
	}

	public Command moveRampUp() {
		return moveRamp(RAMP_UP_POS);
	}

	public Command moveRampDown() {
		return moveRamp(RAMP_DOWN_POS);
	}

	public Command setForward() {
		return instant(() -> setState(IntakeState.FORWARD));
	}

	public Command setReverse() {
		return instant(() -> setState(IntakeState.REVERSE));
	}

	public double getSpeed() {
		return intakeMotor.getThrottle();
	}

	public void cycle() {
		if (intakeState == IntakeState.FORWARD)
			intakeState = IntakeState.REVERSE;
		else if (intakeState == IntakeState.REVERSE)
			intakeState = IntakeState.OFF;
		else
			intakeState = IntakeState.FORWARD;
	}

	private void setState(IntakeState intakeState) {
		this.intakeState = intakeState;
	}

	@Override
	public void periodic() {
		switch (intakeState) {
			case FORWARD :
				intakeMotor.setThrottle(FORWARD);
				break;
			case REVERSE :
				intakeMotor.setThrottle(REVERSE);
				break;
			case OFF :
				intakeMotor.setThrottle(OFF);
				break;
		}
	}
}