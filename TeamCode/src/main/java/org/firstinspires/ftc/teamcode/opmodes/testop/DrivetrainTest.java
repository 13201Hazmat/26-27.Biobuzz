package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Drivetrain Test")
public class DrivetrainTest extends NextOpMode {
	private final HazmatRobot robot;

	public DrivetrainTest(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		Trigger.Companion.getDefaultEventLoop().clear();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);
		robot.getDrivetrain().startDrive(gamepad1);

		gp1.dpadUp().onTrue(instant(() -> robot.getLauncher().increasePower()));
		gp1.dpadDown().onTrue(instant(() -> robot.getLauncher().decreasePower()));
		gp1.triangle().onTrue(instant(() -> robot.getLauncher().test()));

		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));

		gp1.dpadRight().onTrue(instant(() -> robot.getLauncherHood().incrementUp()));
		gp1.dpadLeft().onTrue(instant(() -> robot.getLauncherHood().incrementDown()));

	}

	@Override
	public void periodic() {
		telemetry.addLine("---TELEMETRY---");
		telemetry.addLine("Cycle Transfer: LEFT BUMPER");
		telemetry.addLine("Cycle Intake: RIGHT BUMPER");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Start Launcher: TRIANGLE");
		telemetry.addLine("Increase Launcher Speed: DPAD-UP");
		telemetry.addLine("Decrease Launcher Speed: DPAD-DOWN");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Increase Launcher Hood Position: DPAD-RIGHT");
		telemetry.addLine("Decrease Launcher Hood Position: DPAD-LEFT");
		telemetry.addLine("---------------------------------------------");
		telemetry.addData("Intake Speed:", robot.getIntake().getSpeed());
		telemetry.addData("Launcher Speed:", robot.getLauncher().getPower());
		telemetry.addData("Launcher Hood Position", robot.getLauncherHood().getServoPos());
		telemetry.update();
	}

}
