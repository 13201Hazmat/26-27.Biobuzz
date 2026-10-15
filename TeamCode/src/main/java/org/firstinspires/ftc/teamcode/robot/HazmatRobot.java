package org.firstinspires.ftc.teamcode.robot;

import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private final Intake intake;

	public HazmatRobot() {
		intake = new Intake();
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

	public Command moveToPoint(Path path) {
		return follow(getFollower(), path);
	}

	public Command intakeToPoint(Path intakePath) {
		return parallel(moveToPoint(intakePath), intake.setForward());
	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake);
	}
}