package org.firstinspires.ftc.teamcode.mechanisms.transfer;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

/**
 * Handles the transfer mechanism for managing ball intake and tracking storage
 */
public class Transfer implements Mechanism {

	private static final double FORWARD_SPEED = 1.0;
	private static final double BACKWARD_SPEED = -1.0;
	private static final double IDLE_SPEED = 0.0;

	/**
	 * Represents the capacity state of the transfer system based on stored ball
	 * count.
	 */
	public enum TransferSensingState {
		EMPTY, FILLING, FULL
	}

	/**
	 * Represents the movement state of the transfer motor.
	 */
	public enum TransferMotorState {
		FORWARD, BACKWARD, STOPPED
	}

	private final NextMotor transferMotor = new NextMotor(Config.transferMotorModule, Config.transferMotorPort);
	private TransferMotorState transferMotorState;
	private TransferSensingState transferSensingState;
	private final TransferTracking transferTracking;

	/**
	 * Constructs a new Transfer instance, initializing motor direction, tracking,
	 * and default states (FORWARD and EMPTY).
	 */
	public Transfer() {
		transferMotor.setDirection(NextMotor.Direction.FORWARD);
		transferTracking = new TransferTracking();
		transferMotorState = TransferMotorState.FORWARD;
		transferSensingState = TransferSensingState.EMPTY;
		transferTracking.init();
	}

	/**
	 * Gets the current throttle speed of the transfer motor.
	 *
	 * @return Current motor throttle output.
	 */
	public double getSpeed() {
		return transferMotor.getThrottle();
	}

	/**
	 * Gets the associated {@link TransferTracking} instance.
	 *
	 * @return The sensing and ball tracking object.
	 */
	public TransferTracking getTransferSensing() {
		return this.transferTracking;
	}

	public TransferSensingState getTransferSensingState() {
		return transferSensingState;
	}
	/**
	 * Sets the internal motor state.
	 *
	 * @param transferMotorState
	 *            The target motor state.
	 */
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
		setState(TransferMotorState.STOPPED);
	}

	/**
	 * Command to set the transfer motor state to move forward.
	 *
	 * @return Command triggering state change to FORWARD.
	 */
	public Command setForward() {
		return instant(this::forward);
	}

	/**
	 * Command to set the transfer motor state to move in reverse.
	 *
	 * @return Command triggering state change to BACKWARD.
	 */
	public Command setReverse() {
		return instant(this::reverse);
	}

	/**
	 * Command to set the transfer motor state to stopped.
	 *
	 * @return Command triggering state change to STOPPED.
	 */
	public Command setOff() {
		return instant(this::off);
	}

	/**
	 * Logs current transfer state and throttle speed to Telemetry for debugging.
	 */
	public void debug() {
		transferTracking.debug();
		Telemetry.log("TRANSFER STATE:", transferMotorState);
		Telemetry.log("TRANSFER SPEED", transferMotor.getThrottle());
	}

	/**
	 * Periodic update loop. Evaluates stored ball count, updates sensing state,
	 * adjusts motor state accordingly, and sets motor speed throttle.
	 * <ul>
	 * <li>0 balls &rarr; EMPTY</li>
	 * <li>1-3 balls &rarr; FILLING</li>
	 * <li>4 balls &rarr; FULL</li>
	 * </ul>
	 */
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