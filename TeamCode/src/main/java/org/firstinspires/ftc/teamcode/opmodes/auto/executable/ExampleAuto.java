package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.LeavePoses;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Leave Auto")
public class ExampleAuto extends BaseAuto {
	public ExampleAuto(HazmatRobot robot) {
		super(robot, Alliance.BLUE, LeavePoses.startingPose);
		setAutoCommand(runnables.preloadAuto());
	}
}