package org.firstinspires.ftc.teamcode.mechanisms.transfer;

import static dev.nextftc.units.Units.Amperes;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.telemetry.Telemetry;
import dev.nextftc.units.measuretypes.Current;

public class Transfer implements Mechanism {

	public enum TransferSensingState {
		EMPTY, FILLING, FULL
	}

	public enum TransferMotorState {
		FORWARD, BACKWARD, STOPPED
	}

	private static final double FORWARD_SPEED = 1.0;
	private static final double BACKWARD_SPEED = -1.0;
	private static final double IDLE_SPEED = 0.0;

	private final Current currentLimit = Amperes.of(5.0);

	private final NextMotor transferMotor = new NextMotor(Config.transferMotorModule, Config.transferMotorPort);
	private TransferMotorState transferMotorState;
	private TransferSensingState transferSensingState;
	private final TransferTracking transferTracking;

	public Transfer() {
		transferMotor.setDirection(NextMotor.Direction.FORWARD);
		transferTracking = new TransferTracking();
		transferMotorState = TransferMotorState.FORWARD;
		transferSensingState = TransferSensingState.EMPTY;

		transferMotor.setCurrentAlert(currentLimit);

		transferTracking.init();
	}

	public double getSpeed() {
		return transferMotor.getThrottle();
	}

	public TransferTracking getTransferSensing() {
		return this.transferTracking;
	}

	public TransferSensingState getTransferSensingState() {
		return transferSensingState;
	}

	private void setState(TransferMotorState transferMotorState) {
		this.transferMotorState = transferMotorState;
	}

	public boolean isStalling() {
		return transferMotor.isOverCurrent();
	}

	public void forward() {
		setState(TransferMotorState.FORWARD);
	}

	public void reverse() {
		setState(TransferMotorState.BACKWARD);
	}

	public void off() {
		setState(TransferMotorState.STOPPED);
	}

	public void cycle() {
		if (transferMotorState == TransferMotorState.FORWARD) {
			transferMotorState = TransferMotorState.BACKWARD;
		} else if (transferMotorState == TransferMotorState.BACKWARD) {
			transferMotorState = TransferMotorState.STOPPED;
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
		transferTracking.debug();
		Telemetry.log("TRANSFER STATE:", transferMotorState);
		Telemetry.log("TRANSFER SPEED", transferMotor.getThrottle());
	}

	@Override
	public void periodic() {
		transferTracking.periodic();

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
	}
}