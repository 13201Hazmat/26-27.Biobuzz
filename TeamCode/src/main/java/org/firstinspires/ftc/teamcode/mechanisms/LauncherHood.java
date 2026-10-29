package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class LauncherHood implements Mechanism {
	private final NextServo launcherHoodServo = new NextServo(
			Config.launcherHoodServoModule, Config.launcherHoodServoPort, 0.01
	);

	private final double POLLEN_POS = 0.0;
	private final double NECTAR_POS = 0.0;

	public LauncherHood() {
	}

	public void init() {
		launcherHoodServo.setPosition(POLLEN_POS);
	}

	public double getServoPos() {
		return launcherHoodServo.getPosition();
	}

	public Command setServoPos(double pos) {
		return instant(() -> launcherHoodServo.setPosition(pos));
	}

	public Command setPollenPos() {
		return setServoPos(POLLEN_POS);
	}

	public Command setNectarPos() {
		return setServoPos(NECTAR_POS);
	}

	public Command incrementUp() {
		if (getServoPos() + 0.01 >= 1.0) {
			return setServoPos(1.0);
		}
		return setServoPos(getServoPos() + 0.01);
	}
	public Command incrementDown() {
		if (getServoPos() - 0.01 <= 0.0) {
			return setServoPos(0.0);
		}
		return setServoPos(getServoPos() - 0.01);
	}

	public void debug() {
		Telemetry.log("Hood Servo Pos" + getServoPos());
	}
}
