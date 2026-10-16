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

// this is a transfer sensing subsystem which senses and compares colors
public class TransferTracking implements Mechanism {
	private final NextDigitalSensor startTransferBeamBreak = new NextDigitalSensor("startBeamBreak");
	private final NextDigitalSensor endTransferBeamBreak = new NextDigitalSensor("endBeamBreak");
	private final NextColorDistanceSensor transferColorSensor = new NextColorDistanceSensor("startColorSensor");
	private final ArrayDeque<BallType> storedBalls;

	// Edge detection state variables
	private boolean lastStartBeamState = false;
	private boolean lastEndBeamState = false;

	public TransferTracking() {
		this.storedBalls = new ArrayDeque<>();
	}

	// this returns the ball types based on color (blue nectar, red nectar, pollen, nothing)
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

	public Queue<BallType> getStoredBalls() {
		return storedBalls;
	}

	public BallType getTopBall() {
		return storedBalls.peek();
	}

	public void removeTopBall() {
		if (!storedBalls.isEmpty()) {
			storedBalls.removeFirst();
		}
	}

	public void debug() {
		Telemetry.log("DEBUG", transferColorSensor.debug());
		Telemetry.log("BALL TYPE", BallType.currentBallType);
		Telemetry.log("NUMBER OF BALLS", storedBalls.size());
		Telemetry.log("QUEUE:", storedBalls);
	}

	public void init() {
		storedBalls.clear();
		lastStartBeamState = false;
		lastEndBeamState = false;
	}

	@Override
	public void periodic() {
		transferColorSensor.update();

		boolean currentStartBeamState = startTransferBeamBreak.isTriggered();
		boolean currentEndBeamState = endTransferBeamBreak.isTriggered();

		BallType sensedColor = getBallType();
		BallType.currentBallType = sensedColor;

		if (currentStartBeamState && !lastStartBeamState) {
			if (sensedColor != BallType.NOTHING) {
				storedBalls.addLast(sensedColor);
			}
		}

		if (!currentEndBeamState && lastEndBeamState) {
			if (!storedBalls.isEmpty()) {
				storedBalls.removeFirst();
			}
		}

		lastStartBeamState = currentStartBeamState;
		lastEndBeamState = currentEndBeamState;
	}
}