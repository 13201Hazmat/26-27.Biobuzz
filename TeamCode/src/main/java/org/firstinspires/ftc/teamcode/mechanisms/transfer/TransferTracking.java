package org.firstinspires.ftc.teamcode.mechanisms.transfer;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.data.ColorProfiles;

import java.util.ArrayDeque;
import java.util.Queue;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.hardware.sensors.NextDigitalSensor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

/**
 * Transfer sensing subsystem that detects, tracks, and manages ball types
 * entering and exiting the transfer mechanism using color and beam break
 * sensors.
 */
public class TransferTracking implements Mechanism {
	private final NextDigitalSensor intakeBeamBreak = new NextDigitalSensor(RobotController.controlHub(), 0);
	private final NextDigitalSensor launcherBeamBreak = new NextDigitalSensor(RobotController.controlHub(), 4);
	private final NextColorDistanceSensor transferColorSensor = new NextColorDistanceSensor("startColorSensor");
	private final ArrayDeque<BallType> storedBalls;

	private boolean lastIntakeBeamState = false;
	private boolean lastLauncherBeamState = false;

	/**
	 * Constructs a new TransferTracking instance and initializes the internal ball
	 * storage queue.
	 */
	public TransferTracking() {
		this.storedBalls = new ArrayDeque<>();
	}

	/**
	 * Identifies and returns the detected ball type based on current color sensor
	 * readings.
	 *
	 * @return The detected {@link BallType} (POLLEN, BLUE_NECTAR, RED_NECTAR, or
	 *         NOTHING).
	 */
	public BallType getBallType() {
		if (transferColorSensor.isColor(ColorProfiles.POLLEN_COLOR_PROFILE)) {
			return BallType.POLLEN;
		} else if (transferColorSensor.isColor(ColorProfiles.BLUE_NECTAR_COLOR_PROFILE)) {
			return BallType.BLUE_NECTAR;
		} else if (transferColorSensor.isColor(ColorProfiles.RED_NECTAR_COLOR_PROFILE)) {
			return BallType.RED_NECTAR;
		}

		return BallType.NOTHING;
	}

	/**
	 * Gets the color and distance sensor associated with the transfer system.
	 *
	 * @return The {@link NextColorDistanceSensor} instance.
	 */
	public NextColorDistanceSensor getTransferColorSensor() {
		return transferColorSensor;
	}

	/**
	 * Gets the beam break digital sensor located at the start of the transfer.
	 *
	 * @return The start {@link NextDigitalSensor} instance.
	 */
	public NextDigitalSensor getIntakeBeamBreak() {
		return intakeBeamBreak;
	}

	/**
	 * Gets the queue of currently stored balls in the transfer mechanism.
	 *
	 * @return A {@link Queue} containing the {@link BallType} elements currently
	 *         stored.
	 */
	public Queue<BallType> getStoredBalls() {
		return storedBalls;
	}

	/**
	 * Retrieves the top ball in the storage queue without removing it.
	 *
	 * @return The top {@link BallType}, or {@code null} if the queue is empty.
	 */
	public BallType getTopBall() {
		return storedBalls.peek();
	}

	/**
	 * Removes the top ball from the storage queue if the queue is not empty.
	 */
	public void removeTopBall() {
		if (!storedBalls.isEmpty()) {
			storedBalls.removeFirst();
		}
	}

	/**
	 * Logs debugging information regarding sensor state, ball type, and queue size
	 * to Telemetry.
	 */
	public void debug() {
		Telemetry.log("Color Sensor:", transferColorSensor.debug());
		Telemetry.log("Intake Beam", intakeBeamBreak.isTriggered());
		Telemetry.log("Launcher Beam", launcherBeamBreak.isTriggered());
		Telemetry.log("BALL TYPE:", BallType.currentBallType);
		Telemetry.log("NUMBER OF BALLS:", storedBalls.size());
		Telemetry.log("QUEUE:", storedBalls);

	}

	/**
	 * Initializes hardware state variables and clears the stored ball queue.
	 */
	public void init() {
		storedBalls.clear();
		lastIntakeBeamState = false;
		lastLauncherBeamState = false;
	}

	/**
	 * Periodic update loop that updates sensor states, processes edge transitions
	 * for beam breaks, enqueues newly detected balls at the entrance, and dequeues
	 * balls at the exit.
	 */
	@Override
	public void periodic() {
		transferColorSensor.update();

		boolean currentIntakeBeamState = intakeBeamBreak.isTriggered();
		boolean currentLauncherBeamState = launcherBeamBreak.isTriggered();

		BallType sensedColor = getBallType();
		BallType.currentBallType = sensedColor;

		if (currentIntakeBeamState && !lastIntakeBeamState) {
			if (sensedColor != BallType.NOTHING) {
				storedBalls.addLast(sensedColor);
			}
		}

		if (!currentLauncherBeamState && lastLauncherBeamState) {
			if (!storedBalls.isEmpty()) {
				storedBalls.removeFirst();
			}
		}

		lastIntakeBeamState = currentIntakeBeamState;
		lastLauncherBeamState = currentLauncherBeamState;
	}
}