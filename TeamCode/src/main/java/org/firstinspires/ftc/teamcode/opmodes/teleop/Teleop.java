package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Transfer Test")
public class Teleop extends NextOpMode {
	private final HazmatRobot robot;
	public Teleop(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.getTransfer().init();
		Trigger.Companion.getDefaultEventLoop().clear();
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));
		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));
		gp1.square().onTrue(instant(() -> robot.getTransfer().removeTopBall()));
	}

	@Override
	public void periodic() {
		robot.printTelemetry(telemetry);
		telemetry.update();
	}
}
