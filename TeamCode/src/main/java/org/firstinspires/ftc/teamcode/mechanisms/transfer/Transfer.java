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
	//the transfer is always in one of three states - empty, filling, or full

	public enum TransferSensingState {
		EMPTY, FILLING, FULL
	}
	//transfer is either moving forward, backward, or stopped
	public enum TransferMotorState {
		FORWARD, BACKWARD, STOPPED
	}
	//this constructor method sets the direction and sets the states (forward and full)
	public Transfer() {
		transferMotor.setDirection(NextMotor.Direction.FORWARD);
		transferTracking = new TransferTracking();
		transferMotorState = TransferMotorState.FORWARD;
		transferSensingState = TransferSensingState.EMPTY;
	}
	//this returns the current transfer speed
	public double getSpeed() {
		return transferMotor.getThrottle();
	}

	public TransferTracking getTransferSensing() {
		return this.transferTracking;
	}

	//sets the speed state
	private void setState(TransferMotorState transferMotorState) {
		this.transferMotorState = transferMotorState;
	}
	//this sets the transfer to move forward
	public Command setForward() {
		return instant(() -> setState(TransferMotorState.FORWARD));
	}
	//this sets the transfer to move backwards
	public Command setReverse() {
		return instant(() -> setState(TransferMotorState.BACKWARD));
	}

	//this sets the transfer to stop moving
	public Command setFull() {
		return instant(() -> setState(TransferMotorState.STOPPED));
	}

	//this is where the initialization goes, steps you have to do before running the transfer
	public void init() {
		transferTracking.init();
		currentTriggerState = false;
	}
	//this returns the state and speed of the transfer for debugging
	public void debug() {
		transferTracking.debug();
		Telemetry.log("TRANSFER STATE:", transferMotorState);
		Telemetry.log("TRANSFER SPEED", transferMotor.getThrottle());
	}

	//if 0 balls --> empty
	//if 1-3 balls --> filling
	//if 4 balls --> full
	@Override
	public void periodic() {
		transferTracking.periodic();
		//if there is something in front of the beam break
		boolean isTriggered = transferTracking.getStartTransferBeamBreak().isTriggered();
		//if the transfer is empty or filling --> transfer state forward
		//if the transfer is full --> transfer state stopped
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
		//this sets the speed depending on the transfer state
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