package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;
import org.firstinspires.ftc.teamcode.vision.BallChaseFollower;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Ball Chase Auto")
public class BallChaseAuto extends NextOpMode {
	private final HazmatRobot robot;
	private BallChaseFollower ballFollower;

	public BallChaseAuto(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
	}

	@Override
	public void start() {
		robot.getVision().start();

		if (Alliance.currentAlliance == Alliance.RED) {
			robot.getVision().trackPollenAndRedNectar();
		} else if (Alliance.currentAlliance == Alliance.BLUE) {
			robot.getVision().trackPollenAndBlueNectar();
		} else {
			robot.getVision().trackAll();
		}

		ballFollower = new BallChaseFollower(robot.getFollower(), robot.getVision().getBallTracker(), null);

		ballFollower.setTimeBudgetSec(30.0);
		ballFollower.start();
	}

	@Override
	public void periodic() {
		if (ballFollower != null && ballFollower.isActive()) {
			ballFollower.update();
			ballFollower.addTelemetry(telemetry);
		}
	}
}
