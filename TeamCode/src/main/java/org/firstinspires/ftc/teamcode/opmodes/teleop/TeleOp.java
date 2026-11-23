package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.follower.ManualDrive;

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

		robot.setActiveOpMode(this);

		Alliance.currentAlliance = alliance;

		Trigger.Companion.getDefaultEventLoop().clear();

		robot.setPose(PoseStorage.get());
	}

	@Override
	public void disabledPeriodic() {
		robot.getFollower();
	}

	@Override
	public void start() {
		robot.init().schedule();
		gp1();
		gp2();
	}

	protected void gp1() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		// robot.getDrivetrain().startDrive(gamepad1/* , () -> robot.getPose().heading()
		// */);

		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()).with(instant(robot::setManualTransferTrue)));
		gp1.leftStickButton().onTrue(instant(robot::toggleManualTransfer));
		gp1.rightStickButton().onTrue(robot.getIntake().toggleRamp());
		gp1.square().onTrue(instant(() -> robot.relocalize(Alliance.currentAlliance)));

		gp1.leftTrigger().isOver(0.2).onTrue(instant(() -> robot.setDebugEnabled(true))).onFalse(
			instant(() -> robot.setDebugEnabled(false))
		);

		gp1.rightTrigger().isOver(0.2).onTrue(robot.launch());
	}

	protected void gp2() {
		CommandGamepad gp2 = new CommandGamepad(gamepad2);

		gp2.rightBumper().onTrue(instant(() -> robot.getLauncher().adjustOffset(true)));
		gp2.leftBumper().onTrue(instant(() -> robot.getLauncher().adjustOffset(false)));

		gp2.rightTrigger().isOver(0.2).onTrue(robot.getLauncher().closeGate());

		gp2.dpadDown().onTrue(instant(() -> robot.getTurret().toggleAutoAim()));
		gp2.x().onTrue(instant(() -> robot.getTransfer().cycle()).with(instant(robot::toggleManualTransfer)));

		gp2.leftTrigger().isOver(0.2).onTrue(instant(() -> robot.setDebugEnabled(true))).onFalse(
			instant(() -> robot.setDebugEnabled(false))
		);

	}

	@Override
	public void periodic() {
//		DrivePowers powers = ManualDrive.fieldCentric(
//				-gamepad1.left_stick_y,
//				gamepad1.left_stick_x,
//				gamepad1.right_stick_x,
//				robot.getFollower().pose().heading()
//		);
//
//		ManualDrive.driveOrHold(robot.getFollower(), powers);

		robot.startFieldCentricDrive(gamepad1);
	}
}