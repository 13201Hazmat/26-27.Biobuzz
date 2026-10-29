package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Test Auto")
public class BaseAuto extends NextOpMode {
	protected final HazmatRobot robot;
	protected final Runnables runnables;
	protected final Pose startingPose;
	protected Command autoCommand;

	protected BaseAuto(HazmatRobot robot, Alliance alliance, Pose startingPose) {
		super(robot);
		this.robot = robot;
		this.startingPose = startingPose;
		runnables = new Runnables(robot);

		Alliance.currentAlliance = alliance;
		PathsAndPoses.mirror();
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
