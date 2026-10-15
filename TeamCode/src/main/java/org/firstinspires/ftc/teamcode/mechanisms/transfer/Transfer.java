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
	private final double IDLE_SPEED = 0.0;
	private final NextMotor transferMotor = new NextMotor(Config.transferModule, Config.transferMotorPort);
	private TransferMotorState transferMotorState;
	private TransferSensingState transferSensingState;
	private TransferTracking transferTracking;

	private boolean currentTriggerState = false;

	public enum TransferSensingState {
		EMPTY, FILLING, FULL
	}
	public enum TransferMotorState {
		FORWARD, BACKWARD, STOPPED
	}
	public Transfer() {
		transferMotor.setDirection(NextMotor.Direction.FORWARD);
		transferTracking = new TransferTracking();
		transferMotorState = TransferMotorState.FORWARD;
		transferSensingState = TransferSensingState.EMPTY;
	}
	public double getSpeed() {
		return transferMotor.getThrottle();
	}

	public TransferTracking getTransferSensing() {
		return this.transferTracking;
	}
	private void setState(TransferMotorState transferMotorState) {
		this.transferMotorState = transferMotorState;
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

	public void init() {
		transferTracking.init();
		currentTriggerState = false;
	}
	public void debug() {
		transferTracking.debug();
		Telemetry.log("TRANSFER STATE:", transferMotorState);
		Telemetry.log("TRANSFER SPEED", transferMotor.getThrottle());
	}

	@Override
	public void periodic() {
		transferTracking.periodic();
		boolean isTriggered = transferTracking.getStartTransferBeamBreak().isTriggered();
		switch (transferTracking.getStoredBalls().size()) {
			case 0 :
				transferSensingState = TransferSensingState.EMPTY;
				break;
			case 1 :
			case 2 :
			case 3 :
				transferSensingState = TransferSensingState.FILLING;
				break;
			case 4 :
				transferSensingState = TransferSensingState.FULL;
				break;
		}

		switch (transferSensingState) {
			case EMPTY :
			case FILLING :
				transferMotorState = TransferMotorState.FORWARD;
				break;
			case FULL :
				transferMotorState = TransferMotorState.STOPPED;
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
				transferMotor.setThrottle(IDLE_SPEED);
				break;
		}

		if (isTriggered) {
			if (!currentTriggerState) {
				BallType detected = transferTracking.getBallType();
				if (detected != BallType.NOTHING) {
					transferTracking.getStoredBalls().add(detected);
					currentTriggerState = true;
				}
			}
		} else {
			currentTriggerState = false;
		}
	}
}