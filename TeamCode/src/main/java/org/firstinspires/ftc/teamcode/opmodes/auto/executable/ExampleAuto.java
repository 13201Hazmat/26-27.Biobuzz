package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.LeavePoses;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.HazmatAuto;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class ExampleAuto extends HazmatAuto {
	public ExampleAuto(HazmatRobot robot) {
		super(robot, Alliance.BLUE, LeavePoses.startingPose);
		setAutoCommand(runnables.preloadAuto());
	}
}