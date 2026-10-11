package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.groups.Groups.sequential;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.startingPose_To_audienceFlowerPose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.startingPose_To_leavePose;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class Runners {

	private final HazmatRobot robot;

	public Runners(HazmatRobot robot) {
		this.robot = robot;
	}

	public Command leaveAuto() {
		return sequential(robot.moveToPoint(startingPose_To_leavePose));
	}

	public Command preloadAuto() {
		return sequential(
				// Robot Shoot Command
				robot.moveToPoint(startingPose_To_leavePose));
	}

	public Command audienceFlowerAuto() {
		return sequential(
				// Robot Shoot command for preload

				// Intake to Flower
				robot.flowerIntake(startingPose_To_audienceFlowerPose)
		);
	}

}
