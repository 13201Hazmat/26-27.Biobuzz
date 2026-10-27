package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.*;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Basic Flower Auto")
public class BasicFlowerAuto extends BaseAuto {
	protected BasicFlowerAuto(HazmatRobot robot) {
		super(robot, Alliance.RED, BasicFlowerAutoPoses.startingPose);
	}
}
