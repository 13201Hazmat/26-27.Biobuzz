package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

public class BaseAuto extends NextOpMode {
	protected final HazmatRobot robot;
	protected Runnables runnables;
	protected Pose startingPose;
	protected Command autoCommand;

	protected BaseAuto(HazmatRobot robot, Alliance alliance) {
		super(robot);
		this.robot = robot;

		Alliance.currentAlliance = alliance;

		this.runnables = new Runnables(robot);
		this.startingPose = runnables.paths.leavePoses.startingPose;
	}

	public void setAutoCommand(Command command) {
		this.autoCommand = command;
	}

	@Override
	public void start() {
		robot.getFollower().setPose(startingPose);
		if (autoCommand != null) {
			autoCommand.schedule();
		}
	}

	@Override
	public void periodic() {
		robot.getFollower().update();
		telemetry.addData("POSE", robot.getFollower().pose());
	}
}
