package org.firstinspires.ftc.teamcode.mechanisms.transfer;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.data.ColorProfiles;
import org.firstinspires.ftc.teamcode.data.Config;

import java.util.ArrayDeque;
import java.util.Queue;

import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.hardware.sensors.NextDigitalSensor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.telemetry.Telemetry;

public class TransferTracking implements Mechanism {

	private final NextDigitalSensor intakeBeamBreak = new NextDigitalSensor(
		Config.intakeBeamBreakModule, Config.intakeBeamBreakPort
	);
	private final NextDigitalSensor launcherBeamBreak = new NextDigitalSensor(
		Config.launcherBeamBreakModule, Config.launcherBeamBreakPort
	);
	private final NextColorDistanceSensor transferColorSensor = new NextColorDistanceSensor(
		Config.intakeColorSensorName
	);
	private final ArrayDeque<BallType> storedBalls;

	private boolean lastIntakeBeamState = false;
	private boolean lastLauncherBeamState = false;

	public TransferTracking() {
		this.storedBalls = new ArrayDeque<>();
	}

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

	public NextDigitalSensor getIntakeBeamBreak() {
		return intakeBeamBreak;
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

	public void init() {
		storedBalls.clear();
		lastIntakeBeamState = false;
		lastLauncherBeamState = false;
	}

	public void debug() {
		Telemetry.log("Color Sensor:", transferColorSensor.debug());
		Telemetry.log("Intake Beam", intakeBeamBreak.isTriggered());
		Telemetry.log("Launcher Beam", launcherBeamBreak.isTriggered());
		Telemetry.log("BALL TYPE:", BallType.currentBallType);
		Telemetry.log("NUMBER OF BALLS:", storedBalls.size());
		Telemetry.log("QUEUE:", storedBalls);
	}

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