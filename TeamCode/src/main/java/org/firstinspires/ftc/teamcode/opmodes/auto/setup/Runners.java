package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.groups.Groups.sequential;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.firstFlowerPose_To_otherSideShootingPose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.startingPose_To_firstFlowerPose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.startingPose_To_leavePose;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class Runners {

	private final HazmatRobot robot;

	public Runners(HazmatRobot robot) {
		this.robot = robot;
	}

	public Command leaveAuto() {
		return robot.moveToPoint(startingPose_To_leavePose);
	}

	public Command preloadAuto() {
		return sequential(
				// SHOOT COMMAND
				robot.moveToPoint(startingPose_To_leavePose));
	}

	public Command basicFlowerAuto() {
		return sequential(
		// SHOOT COMMAND
		// Intake to Flower
		// Go to shoot other side
		// SHOOT COMMAND

		);
	}

}
