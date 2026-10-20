package org.firstinspires.ftc.teamcode.mechanisms.vision;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.hardware.webcams.NextLimelight;
import dev.nextftc.robot.Mechanism;

public class Vision implements Mechanism {
	private final NextLimelight limelight = new NextLimelight("Limelight");
	private final NextServo limelightServo = new NextServo(Config.aModule, Config.aPort);

	private final double INTAKE_POS = 0.0;
	private final double BUCKET_POS = 0.0;

	private BallTracker ballTracker;

	public Vision() {
	}

	private void setPosition(double x) {
		limelightServo.setPosition(x);
	}

	public Command setIntakePos() {
		return instant(() -> setPosition(INTAKE_POS));
	}
	public Command setBucketPos() {
		return instant(() -> setPosition(BUCKET_POS));
	}
	public void start() {
		limelight.startReading(2, 100);
	}

	public void setTracking() {
		limelight.setPipeline(2);
	}

	public void setCounting() {
		limelight.setPipeline(3);
	}

	public NextLimelight getLimelight() {
		return limelight;
	}

	public BallTracker getBallTracker() {
		if (ballTracker == null) {
			ballTracker = new BallTracker(limelight);
		}
		return ballTracker;
	}

	public void trackPollenAndRedNectar() {
		getBallTracker().setAllowedClasses(BallTracker.CLASS_YELLOW_NEUTRAL, BallTracker.CLASS_RED);
	}

	public void trackPollenAndBlueNectar() {
		getBallTracker().setAllowedClasses(BallTracker.CLASS_YELLOW_NEUTRAL, BallTracker.CLASS_BLUE);
	}

	public void trackAll() {
		getBallTracker().setAllowedClasses(BallTracker.CLASSES_ALL);
	}
}
