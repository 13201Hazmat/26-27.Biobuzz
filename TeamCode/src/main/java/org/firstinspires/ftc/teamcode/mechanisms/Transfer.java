package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.data.ColorProfiles;
import org.firstinspires.ftc.teamcode.data.Config;

import java.util.ArrayDeque;
import java.util.Queue;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.hardware.sensors.NextDigitalSensor;
import dev.nextftc.hardware.sensors.colors.NextColor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Transfer implements Mechanism {
	private static final double FORWARD_SPEED = 1.0;
	private static final double BACKWARD_SPEED = -1.0;

	private final NextMotor transferMotor = new NextMotor(Config.aModule, Config.aPort);
	private TransferState transferState;
	private final NextDigitalSensor transferBeamBreak = new NextDigitalSensor(Config.bModule, Config.bPort, true);
	private final NextColorDistanceSensor transferColorSensor = new NextColorDistanceSensor(Config.cModule,
			Config.cPort, true);

	private boolean lastBeamBreakState = false;

	private final ArrayDeque<BallType> storedBalls;

	public enum TransferState {
		FORWARD, BACKWARD, FULL
	}

	public Transfer() {
		transferMotor.setDirection(NextMotor.Direction.FORWARD);
		transferState = TransferState.FORWARD;
		storedBalls = new ArrayDeque<>();
	}

	private void setState(TransferState transferState) {
		this.transferState = transferState;
	}

	public double getSpeed() {
		return transferMotor.getThrottle();
	}

	public Command setForward() {
		return instant(() -> setState(TransferState.FORWARD));
	}

	public Command setReverse() {
		return instant(() -> setState(TransferState.BACKWARD));
	}

	public Command setFull() {
		return instant(() -> setState(TransferState.FULL));
	}

	public BallType detectColor() {
		NextColor detectedColor = transferColorSensor.getColor();
		if (ColorProfiles.BLUE_NECTAR_COLOR_PROFILE.matches(detectedColor)) {
			return BallType.BLUE_NECTAR;
		} else if (ColorProfiles.RED_NECTAR_COLOR_PROFILE.matches(detectedColor)) {
			return BallType.RED_NECTAR;
		} else if (ColorProfiles.POLLEN_COLOR_PROFILE.matches(detectedColor)) {
			return BallType.POLLEN;
		}
		return BallType.NOTHING;
	}

	public Queue<BallType> getStoredBalls() {
		return storedBalls;
	}

	public BallType getTopBall() {
		return storedBalls.peek();
	}

	public void removeTopBall() {
		storedBalls.removeFirst();
	}

	public boolean isFull() {
		return storedBalls.size() == 4;
	}

	public void printDebugMessages() {
		Telemetry.log("TRANSFER TELEMETRY");
		Telemetry.log("Motor Speed:", transferMotor.getThrottle());
		Telemetry.log("Ball Color:", transferColorSensor.getColor());
		Telemetry.log("Top Ball:", getTopBall());
	}

	@Override
	public void periodic() {
		transferColorSensor.update();
		Telemetry.update();
		switch (transferState) {
			case FORWARD :
				transferMotor.setThrottle(FORWARD_SPEED);
				break;
			case BACKWARD :
				transferMotor.setThrottle(BACKWARD_SPEED);
				break;
			case FULL :
				transferMotor.setThrottle(0.0);
				break;
		}

		boolean currentBeamBreakState = transferBeamBreak.getRawState();

		if (currentBeamBreakState && !lastBeamBreakState) {
			BallType detected = detectColor();
			if (detected != BallType.NOTHING) {
				storedBalls.add(detected);
			}
		}

		lastBeamBreakState = currentBeamBreakState;
	}
}