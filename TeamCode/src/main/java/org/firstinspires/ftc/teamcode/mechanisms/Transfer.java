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

public class Transfer implements Mechanism {
	private static final double FORWARD_SPEED = 1.0;
	private static final double BACKWARD_SPEED = -1.0;
	private final NextMotor transferMotor = new NextMotor(Config.aModule, Config.aPort);
	private TransferState transferState;
	// Inverted means that when detected it will return true
	private final NextDigitalSensor transferBeamBreak = new NextDigitalSensor(Config.bModule, Config.bPort, true);
	private final NextColorDistanceSensor transferColorSensor = new NextColorDistanceSensor(Config.cModule,
			Config.cPort, true);
	// Order in which the balls are stored and released.
	private final Queue<BallType> storedBalls;

	public enum TransferState {
		// Moving forward
		FORWARD,
		// Moving backward
		BACKWARD,
		// Full, not moving at all
		FULL
	}

	public Transfer() {
		// TODO SWITCH THIS VALUE IF NEEDED
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
		return null;
	}

	@Override
	public void periodic() {
		switch (transferState) {
			case FORWARD :
				transferMotor.setThrottle(FORWARD_SPEED);
			case BACKWARD :
				transferMotor.setThrottle(BACKWARD_SPEED);
			case FULL :
				transferMotor.setThrottle(0.0);
		}
		storedBalls.add(detectColor());
	}
}