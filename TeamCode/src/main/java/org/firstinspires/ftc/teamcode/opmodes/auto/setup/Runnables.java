package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class Runnables {

	private final HazmatRobot robot;
	public final PathsAndPoses pathsAndPoses;

	public Runnables(HazmatRobot robot) {
		this.robot = robot;
		this.pathsAndPoses = new PathsAndPoses();
	}

	public Command decidePath(boolean condition, Path option1, Path option2) {
		return conditional(() -> condition, robot.moveToPoint(option1), robot.moveToPoint(option2));
	}

	public Command leaveAuto(boolean isFront) {
		return decidePath(
				isFront,
				pathsAndPoses.leavePoses.startingPose_To_frontLeavePose,
				pathsAndPoses.leavePoses.startingPose_To_backLeavePose
		);
	}

	public Command preloadAuto(boolean isFront) {
		return sequential(
				robot.moveToPoint(pathsAndPoses.leavePoses.startingPose_To_midPose),
				waitMs(1000),
				decidePath(
						isFront,
						pathsAndPoses.leavePoses.midPose_To_frontLeavePose,
						pathsAndPoses.leavePoses.midPose_To_backLeavePose
				)
		);
	}

	public Command basicFlowerAuto(boolean inMiddle, boolean inFrontPark) {
		return sequential(
				robot.moveToPoint(pathsAndPoses.basicFlowerAutoPoses.startingPose_To_midPose),
				waitMs(1000),
				decidePath(
						inMiddle,
						pathsAndPoses.basicFlowerAutoPoses.midPose_To_shootPose_MID,
						pathsAndPoses.basicFlowerAutoPoses.midPose_To_shootPose
				),
				waitMs(1000),
				robot.moveToPoint(pathsAndPoses.basicFlowerAutoPoses.shootPose_To_endFlowerPose),
				waitMs(1000),
				decidePath(
						inFrontPark,
						pathsAndPoses.basicFlowerAutoPoses.endFlowerPose_To_frontLeavePose,
						pathsAndPoses.basicFlowerAutoPoses.endFlowerPose_To_backLeavePose
				)
		);
	}

	public Command doubleFlowerAuto(boolean inMiddle, boolean inFrontPark) {
		return sequential();
	}
}
