package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private final Intake intake;
	private final Drivetrain drivetrain;

	public HazmatRobot() {
		intake = new Intake();
		drivetrain = new Drivetrain(getFollower());
	}

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	public Intake getIntake() {
		return intake;
	}

	public Drivetrain getDrivetrain() {
		return drivetrain;
	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, drivetrain);
	}
}