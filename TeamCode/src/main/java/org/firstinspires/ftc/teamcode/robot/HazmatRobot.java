package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Vision;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private Vision vision;
	private Drivetrain drivetrain;

	public HazmatRobot() {
		vision = new Vision();
	}

	public Vision getVision() {
		return vision;
	}


	public Drivetrain getDrivetrain() {
		return drivetrain;
	}

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(vision, drivetrain);
	}
}