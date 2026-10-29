package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.*;

import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class Runnables {

	private final HazmatRobot robot;

	public Runnables(HazmatRobot robot) {
		this.robot = robot;
	}

	public Command decidePath(boolean condition, Path option1, Path option2) {
		return conditional(() -> condition, robot.moveToPoint(option1), robot.moveToPoint(option2));
	}

	public Command leaveAuto(boolean isFront) {
		return decidePath(isFront, LeavePoses.startingPose_To_frontLeavePose, LeavePoses.startingPose_To_backLeavePose);
	}

	public Command preloadAuto(boolean isFront) {
		return sequential(
				robot.moveToPoint(LeavePoses.startingPose_To_midPose),
				waitMs(1000),
				decidePath(isFront, LeavePoses.midPose_To_frontLeavePose, LeavePoses.midPose_To_backLeavePose)
		);
	}

	public Command basicFlowerAuto(boolean inMiddle, boolean inFrontPark) {
		return sequential(
				robot.moveToPoint(BasicFlowerAutoPoses.startingPose_To_midPose),
				waitMs(1000),
				decidePath(
						inMiddle,
						BasicFlowerAutoPoses.midPose_To_shootPose_MID,
						BasicFlowerAutoPoses.midPose_To_shootPose
				),
				waitMs(1000),
				robot.moveToPoint(BasicFlowerAutoPoses.shootPose_To_endFlowerPose),
				waitMs(1000),
				decidePath(
						inFrontPark,
						BasicFlowerAutoPoses.endFlowerPose_To_frontLeavePose,
						BasicFlowerAutoPoses.endFlowerPose_To_backLeavePose
				)
		);
	}

	public Command doubleFlowerAuto(boolean inMiddle, boolean inFrontPark) {
		return sequential(

		);
	}
}
