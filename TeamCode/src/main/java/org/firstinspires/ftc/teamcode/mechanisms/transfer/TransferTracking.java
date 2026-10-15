package org.firstinspires.ftc.teamcode.mechanisms.transfer;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.data.ColorProfiles;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.hardware.sensors.NextDigitalSensor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

//this is a transfer sensing subsystem which senses and compares colors
public class TransferTracking implements Mechanism {
	private final NextDigitalSensor startTransferBeamBreak = new NextDigitalSensor("startBeamBreak");
	private final NextDigitalSensor endTransferColorSensor = new NextDigitalSensor("endBeamBreak");
	private final NextColorDistanceSensor transferColorSensor = new NextColorDistanceSensor("startColorSensor");
	private final ArrayDeque<BallType> storedBalls;

	public TransferTracking() {
		this.storedBalls = new ArrayDeque<>();
	}

	// this returns the ball types based on color (blue nectar, red nectar, pollen,
	// nothing)
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

	public NextColorDistanceSensor getTransferColorSensor() {
		return transferColorSensor;
	}

	public NextDigitalSensor getStartTransferBeamBreak() {
		return startTransferBeamBreak;
	}

	/* QUEUE METHODS */
	// returns all the balls in the transfer
	public Queue<BallType> getStoredBalls() {
		return storedBalls;
	}

	// returns the first ball we intake
	public BallType getTopBall() {
		return storedBalls.peek();
	}

	// removes the ball when we shoot it
	public void removeTopBall() {
		try {
			storedBalls.removeFirst();
		} catch (NoSuchElementException e) {
			// bl
		}
	}

	/* SUBSYSTEM METHODS */

	// this returns the ball type, number of balls, and the queue for debugging
	public void debug() {
		Telemetry.log("DEBUG", transferColorSensor.debug());
		Telemetry.log("BALL TYPE", BallType.currentBallType);
		Telemetry.log("NUMBER OF BALLS", storedBalls.size());
		Telemetry.log("QUEUE:", storedBalls);
	}

	// clears the array so that we don't have previous matches' data
	public void init() {
		storedBalls.clear();
	}

	// always updating the color sensor and the current ball type
	@Override
	public void periodic() {
		transferColorSensor.update();
		BallType.currentBallType = getBallType();

		if (endTransferColorSensor.isTriggered()) {
			storedBalls.removeFirst();
		}
	}
}
