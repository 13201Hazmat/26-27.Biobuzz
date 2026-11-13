package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.data.PoseStorage;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.BulkReadHook;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

public abstract class TeleOp extends NextOpMode {
	private final HazmatRobot robot;
	public TeleOp(HazmatRobot robot, Alliance alliance) {
		super(robot, BulkReadHook.INSTANCE);
		this.robot = robot;
		robot.getFollower().setPose(new Pose(30.43, 7.48, 180));

		robot.setActiveOpMode(this);

		Alliance.currentAlliance = alliance;

		Trigger.Companion.getDefaultEventLoop().clear();

		robot.setStartingPose(PoseStorage.get());
	}

	@Override
	public void start() {
		gp1();
		gp2();
	}

	protected void gp1() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		robot.getDrivetrain().startDrive(gamepad1);

		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()).with(instant(robot::setManualTransferTrue)));
		gp1.leftStickButton().onTrue(instant(robot::toggleManualTransfer));
		gp1.rightStickButton().onTrue(robot.getIntake().toggleRamp());

		gp1.rightTrigger().isOver(0.2).onTrue(robot.launch());
	}

	protected void gp2() {
		CommandGamepad gp2 = new CommandGamepad(gamepad2);

		gp2.rightBumper().onTrue(instant(() -> robot.getLauncher().adjustOffset(true)));
		gp2.leftBumper().onTrue(instant(() -> robot.getLauncher().adjustOffset(false)));

		gp2.rightTrigger().isOver(0.2).onTrue(robot.getLauncher().closeGate());

		gp2.dpadUp().onTrue(instant(robot::toggleHoodAutomate));
		gp2.dpadRight().onTrue(robot.getLauncherHood().setPollenPos());
		gp2.dpadLeft().onTrue(robot.getLauncherHood().setNectarPos());

		gp2.dpadDown().onTrue(instant(() -> robot.getTurret().toggleAutoAim()));
		gp2.x().onTrue(instant(() -> robot.getTransfer().cycle()).with(instant(robot::toggleManualTransfer)));

		gp2.leftTrigger().isOver(0.2).onTrue(instant(()->robot.setDebugEnabled(true))).onFalse(instant(()-> robot.setDebugEnabled(false)));

	}

	@Override
	public void periodic() {
		telemetry.addData("POSE", robot.getFollower().pose());
		telemetry.update();
	}
}
