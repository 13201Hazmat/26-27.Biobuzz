package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.telemetry.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Drivetrain Test")
public class DrivetrainTest extends NextOpMode {
	private final HazmatRobot robot;

	public DrivetrainTest(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		robot.setActiveOpMode(this);

		Trigger.Companion.getDefaultEventLoop().clear();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		robot.getDrivetrain().startDrive(gamepad1);

		gp1.leftTrigger().isOver(0.2).onTrue(instant(() -> robot.setDebugEnabled(true))).onFalse(
			instant(() -> robot.setDebugEnabled(false))
		);
	}

	@Override
	public void periodic() {
		Telemetry.log("Right Stick X Value", gamepad1.right_stick_x);
		Telemetry.log("Right Stick Y Value", gamepad1.right_stick_y);
		Telemetry.log("Left Stick X Value", gamepad1.left_stick_x);
		Telemetry.log("Left Stick Y Value", gamepad1.left_stick_y);
		Telemetry.log("Left Trigger", "Debug");
	}

}
