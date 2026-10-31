package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.waitMs;
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
		return decidePath(isFront, paths.leavePoses.startingPose_To_frontLeavePose, paths.leavePoses.startingPose_To_backLeavePose);
	}

	public Command preloadAuto(boolean isFront) {
		return sequential(
				robot.moveToPoint(paths.leavePoses.startingPose_To_midPose),
				waitMs(1000),
				decidePath(
						isFront,
						paths.leavePoses.midPose_To_frontLeavePose,
						paths.leavePoses.midPose_To_backLeavePose
				)
		);
	}

	public Command basicFlowerAuto(boolean inMiddle, boolean inFrontPark) {
		return sequential(
				robot.moveToPoint(paths.basicFlowerAutoPoses.startingPose_To_midPose),
				waitMs(1000),
				decidePath(
						inMiddle,
						paths.basicFlowerAutoPoses.midPose_To_shootPose_MID,
						paths.basicFlowerAutoPoses.midPose_To_shootPose
				),
				waitMs(1000),
				robot.moveToPoint(paths.basicFlowerAutoPoses.shootPose_To_endFlowerPose),
				waitMs(1000),
				decidePath(
						inFrontPark,
						paths.basicFlowerAutoPoses.endFlowerPose_To_frontLeavePose,
						paths.basicFlowerAutoPoses.endFlowerPose_To_backLeavePose
				)
		);
	}

	public Command doubleFlowerAuto(boolean inFrontPark) {
		return sequential(
				robot.moveToPoint(paths.doubleFlowerAutoPoses.startingPose_To_firstFlowerPose),
				waitMs(1000),
				robot.moveToPoint(paths.doubleFlowerAutoPoses.firstFlowerPose_To_startSecondFlowerPose),
				waitMs(1000),
				robot.moveToPoint(paths.doubleFlowerAutoPoses.startSecondFlowerPose_To_endSecondFlowerPose),
				waitMs(1000),
				decidePath(
						inFrontPark,
						paths.doubleFlowerAutoPoses.endSecondFlowerPose_To_frontLeavePose,
						paths.doubleFlowerAutoPoses.endSecondFlowerPose_To_backLeavePose
				)
		);
	}
}
