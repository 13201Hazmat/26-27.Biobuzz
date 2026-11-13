package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import java.util.function.BooleanSupplier;

public class Runnables {

	private final HazmatRobot robot;
	public final PathsAndPoses paths;

	public Runnables(HazmatRobot robot) {
		this.robot = robot;
		this.paths = new PathsAndPoses();
	}

	/**
	 * Defers path selection until runtime execution using lazy(...).
	 * Dynamically evaluates condition at runtime and executes only the selected path command.
	 */
	public Command decidePath(BooleanSupplier condition, Path option1, Path option2) {
		return lazy(() -> condition.getAsBoolean() ? robot.moveToPoint(option1) : robot.moveToPoint(option2));
	}

	/* FRONT AUTOS (We are shooting first) */

	public Command move(Path path) {
		return robot.moveToPoint(path);
	}

	public Command intake(Path path) {
		return sequential(
				parallel(robot.moveToPoint(path), robot.getIntake().setForward()),
				waitMs(1000),
				robot.getIntake().setOff()
		);
	}

	public Command leaveAuto(BooleanSupplier isFront) {
		return decidePath(
				isFront,
				paths.leavePoses.startingPose_To_frontLeavePose(),
				paths.leavePoses.startingPose_To_backLeavePose()
		);
	}

	public Command preloadAuto(BooleanSupplier isFront, BooleanSupplier goThroughMiddle) {
		return sequential(
			intake(paths.cp.startingPose_To_gardenIntakePose()),
			waitMs(1000),
			conditional(
				goThroughMiddle,
				// if true go through the middle
				sequential(
					move(paths.leavePoses.midLeavePoses.midPose_To_midLeavePose()),
					decidePath(
						isFront,
						paths.leavePoses.midLeavePoses.midLeavePose_To_frontLeavePose(),
						paths.leavePoses.midLeavePoses.midLeavePose_To_backLeavePose()
					)
				),
				// if false dont go through middle
				sequential(
					decidePath(
						isFront,
						paths.leavePoses.midPose_To_frontLeavePose(),
						paths.leavePoses.midPose_To_backLeavePose()
					)
				)
			)
		);
	}
}