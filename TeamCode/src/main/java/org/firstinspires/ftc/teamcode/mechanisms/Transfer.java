package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.Amperes;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.sensors.NextDigitalSensor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.telemetry.Telemetry;
import dev.nextftc.units.measuretypes.Current;

public class Transfer implements Mechanism {

	public enum TransferMotorState {
		FORWARD, BACKWARD, OFF
	}

	private static final double FORWARD_SPEED = 1.0;
	private static final double BACKWARD_SPEED = -1.0;
	private static final double OFF_SPEED = 0.0;

	private final Current currentLimit = Amperes.of(5.0);

	private final NextMotor transferMotor = new NextMotor(Config.transferMotorModule, Config.transferMotorPort);
	private final NextDigitalSensor intakeBeamBreak = new NextDigitalSensor(
			Config.intakeBeamBreakModule, Config.intakeBeamBreakPort, false
	);

	private TransferMotorState transferMotorState = TransferMotorState.OFF;
	private int ballCount = 0;
	private boolean lastIntakeBeamState = false;

	public Transfer() {
		transferMotor.setDirection(NextMotor.Direction.FORWARD);
		setCount(0);
	}

	public double getSpeed() {
		return transferMotor.getThrottle();
	}

	public NextDigitalSensor getIntakeBeamBreak() {
		return intakeBeamBreak;
	}

	public int getCount() {
		return ballCount;
	}

	public void setCount(int count) {
		ballCount = count;
	}

	public void increaseCount() {
		ballCount++;
	}

	public void decreaseCount() {
		ballCount--;
	}

	private void setState(TransferMotorState transferMotorState) {
		this.transferMotorState = transferMotorState;
	}

	public void forward() {
		setState(TransferMotorState.FORWARD);
	}

	public void reverse() {
		setState(TransferMotorState.BACKWARD);
	}

	public void off() {
		setState(TransferMotorState.OFF);
	}

	public void cycle() {
		if (transferMotorState == TransferMotorState.FORWARD) {
			transferMotorState = TransferMotorState.BACKWARD;
		} else if (transferMotorState == TransferMotorState.BACKWARD) {
			transferMotorState = TransferMotorState.OFF;
		} else {
			transferMotorState = TransferMotorState.FORWARD;
		}
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

	public void debug() {
		Telemetry.log("Intake Beam", intakeBeamBreak.isTriggered());
		Telemetry.log("BALL TYPE:", BallType.currentBallType);
		Telemetry.log("NUMBER OF BALLS:", ballCount);
		Telemetry.log("TRANSFER STATE:", transferMotorState);
		Telemetry.log("TRANSFER SPEED", transferMotor.getThrottle());
	}

	private void updateBallCount() {
		boolean currentIntakeBeamState = intakeBeamBreak.isTriggered();

		if (currentIntakeBeamState && !lastIntakeBeamState) {
			increaseCount();
		}

		lastIntakeBeamState = currentIntakeBeamState;
	}

	@Override
	public void periodic() {
		updateBallCount();

		switch (transferMotorState) {
			case FORWARD:
				transferMotor.setThrottle(FORWARD_SPEED);
				break;
			case BACKWARD:
				transferMotor.setThrottle(BACKWARD_SPEED);
				break;
			case OFF:
				transferMotor.setThrottle(OFF_SPEED);
				break;
		}
	}
}