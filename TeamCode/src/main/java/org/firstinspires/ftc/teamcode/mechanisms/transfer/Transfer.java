package org.firstinspires.ftc.teamcode.mechanisms.transfer;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Transfer implements Mechanism {
	private final double FORWARD_SPEED = 1.0;
	private final double BACKWARD_SPEED = -1.0;
	private final NextMotor transferMotor = new NextMotor(Config.transferModule, Config.transferMotorPort);
	private TransferMotorState transferMotorState;
	private TransferFullingState transferFullingState;
	private TransferSensing transferSensing;

	private boolean ballAddedForCurrentTrigger = false;

	public enum TransferFullingState {
		EMPTY, FILLING, FULL
	}

	public enum TransferMotorState {
		FORWARD, BACKWARD, STOPPED
	}

	public Transfer() {
		transferMotor.setDirection(NextMotor.Direction.FORWARD);
		transferSensing = new TransferSensing();
		transferMotorState = TransferMotorState.FORWARD;
		transferFullingState = TransferFullingState.FULL;
	}

	private void setState(TransferMotorState transferMotorState) {
		this.transferMotorState = transferMotorState;
	}

	public double getSpeed() {
		return transferMotor.getThrottle();
	}

	public Command setForward() {
		return instant(() -> setState(TransferMotorState.FORWARD));
	}

	public Command setReverse() {
		return instant(() -> setState(TransferMotorState.BACKWARD));
	}

	public Command setFull() {
		return instant(() -> setState(TransferMotorState.STOPPED));
	}

	public TransferSensing getTransferSensing() {
		return this.transferSensing;
	}

	public void init() {
		transferSensing.init();
		ballAddedForCurrentTrigger = false;
	}

	public void debug() {
		transferSensing.debug();
		Telemetry.log("TRANSFER STATE:", transferMotorState);
		Telemetry.log("TRANSFER SPEED", transferMotor.getThrottle());
	}

	@Override
	public void periodic() {
		transferSensing.periodic();

		switch (transferSensing.getStoredBalls().size()) {
			case 0 :
				transferFullingState = TransferFullingState.EMPTY;
				break;
			case 1 :
			case 2 :
			case 3 :
				transferFullingState = TransferFullingState.FILLING;
				break;
			case 4 :
				transferFullingState = TransferFullingState.FULL;
				break;
		}

		switch (transferMotorState) {
			case FORWARD :
				transferMotor.setThrottle(FORWARD_SPEED);
				break;
			case BACKWARD :
				transferMotor.setThrottle(BACKWARD_SPEED);
				break;
			case STOPPED :
				transferMotor.setThrottle(0.0);
				break;
		}

		boolean isTriggered = transferSensing.getTransferBeamBreak().isTriggered();

		if (isTriggered) {
			if (!ballAddedForCurrentTrigger) {
				BallType detected = transferSensing.getBallType();
				if (detected != BallType.NOTHING) {
					transferSensing.getStoredBalls().add(detected);
					ballAddedForCurrentTrigger = true;
				}
			}
		} else {
			ballAddedForCurrentTrigger = false;
		}
	}
}