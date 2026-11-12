package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class Runnables {

	private final HazmatRobot robot;
	public final PathsAndPoses paths;

	public Command decidePath(boolean condition, Path option1, Path option2) {
		return conditional(() -> condition, robot.moveToPoint(option1), robot.moveToPoint(option2));
	}

	public Runnables(HazmatRobot robot) {
		this.robot = robot;
		this.paths = new PathsAndPoses();
	}

	/* FRONT AUTOS (We are shooting first) */

	public Command leaveAuto(boolean isFront) {
		return decidePath(
			isFront, paths.leavePoses.startingPose_To_frontLeavePose(), paths.leavePoses.startingPose_To_backLeavePose()
		);
	}

	public Command move(Path path) {
		return robot.moveToPoint(path);
	}

	public Command intake(Path path) {
		return sequential(
			parallel(robot.moveToPoint(path), robot.getIntake().setForward()), waitMs(1000), robot.getIntake().setOff()
		);
	}

	public Command preloadAuto(boolean isFront, boolean goThroughMiddle) {
		return sequential(
			intake(paths.leavePoses.startingPose_To_midPose()),
			waitMs(1000),
			conditional(
				() -> goThroughMiddle,
				// if true go through the middle
				sequential(
					move(paths.leavePoses.midLeavePoses.midPose_To_midLeavePose()),
					decidePath(
						isFront, paths.leavePoses.midLeavePoses.midLeavePose_To_frontLeavePose(),
						paths.leavePoses.midLeavePoses.midLeavePose_To_backLeavePose()
					)
				),
				// if false dont go through middle
				sequential(
					decidePath(
						isFront, paths.leavePoses.midPose_To_frontLeavePose(),
						paths.leavePoses.midPose_To_backLeavePose()
					)
				)
			)
		);
	}
}