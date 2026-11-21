package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runnables;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Base Auto")
public class BasicAuto extends NextOpMode {
	protected final HazmatRobot robot;
	protected Runnables runnables;
	protected Pose startingPose;
	public BasicAuto(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		robot.getTurret().setAutoAim(false);

		Alliance.currentAlliance = Alliance.BLUE;

		this.runnables = new Runnables(robot);
		this.startingPose = runnables.paths.cp.startingPose;

		robot.getFollower();
	}

	@Override
	public void start() {
		robot.getFollower().setPose(startingPose);
		runnables.preloadAuto(() -> true, () -> false).schedule();
	}

	@Override
	public void periodic() {
		robot.getFollower().update();
		telemetry.addData("POSE", robot.getFollower().pose());
		telemetry.addData("ALLIANCE", Alliance.currentAlliance);
		telemetry.update();
	}
}
