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
		return decidePath(isFront, paths.leavePoses.path1, paths.leavePoses.path2);
	}

	public Command preloadAuto(boolean isFront) {
		return sequential(
				robot.moveToPoint(paths.leavePoses.path3),
				waitMs(1000),
				decidePath(isFront, paths.leavePoses.path4FrontLeavePose, paths.leavePoses.path4BackLeavePose)
		);
	}

	public Command basicFlowerAuto(boolean inMiddle, boolean inFrontPark) {
		return sequential(
				robot.moveToPoint(paths.basicFlowerAutoPoses.path1),
				waitMs(1000),
				decidePath(inMiddle, paths.basicFlowerAutoPoses.path2, paths.basicFlowerAutoPoses.path3),
				waitMs(1000),
				robot.moveToPoint(paths.basicFlowerAutoPoses.path4),
				waitMs(1000),
				decidePath(
						inFrontPark,
						paths.basicFlowerAutoPoses.path5FrontLeavePose,
						paths.basicFlowerAutoPoses.path5BackLeavePose
				)
		);
	}

	public Command doubleFlowerAuto(boolean inFrontPark) {
		return sequential(
				robot.moveToPoint(paths.doubleFlowerAutoPoses.path1),
				waitMs(1000),
				robot.moveToPoint(paths.doubleFlowerAutoPoses.path2),
				waitMs(1000),
				robot.moveToPoint(paths.doubleFlowerAutoPoses.path3),
				waitMs(1000),
				decidePath(
						inFrontPark,
						paths.doubleFlowerAutoPoses.path4FrontLeavePose,
						paths.doubleFlowerAutoPoses.path4BackLeavePose
				)
		);
	}

	public Command doubleFlowerAutoWithGarden(boolean isFront) {
		return sequential(
				robot.moveToPoint(paths.doubleFlowerWithGardenAutoPoses.path1()),
				robot.moveToPoint(paths.doubleFlowerWithGardenAutoPoses.path2()),
				robot.moveToPoint(paths.doubleFlowerWithGardenAutoPoses.path3()),
				robot.moveToPoint(paths.doubleFlowerWithGardenAutoPoses.path4()),
				robot.moveToPoint(paths.doubleFlowerWithGardenAutoPoses.path5()),
				robot.moveToPoint(paths.doubleFlowerWithGardenAutoPoses.path6()),
				robot.moveToPoint(paths.doubleFlowerWithGardenAutoPoses.path7())
		);
	}
}
