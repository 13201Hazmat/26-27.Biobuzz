package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Turret Test")
public class TurretTest extends NextOpMode {
	private final HazmatRobot robot;

	public TurretTest(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		robot.setUtilityOpMode(true);


		Trigger.Companion.getDefaultEventLoop().clear();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));

		gp1.dpadLeft().onTrue(instant(() -> robot.getTurret().setPositions(0.0)));
		gp1.dpadRight().onTrue(instant(() -> robot.getTurret().setPositions(1.0)));

		gp1.dpadUp().onTrue(robot.getTurret().incrementTurret());
		gp1.dpadDown().onTrue(robot.getTurret().decrementTurret());
	}

	@Override
	public void periodic() {
		telemetry.addLine("---TELEMETRY---");
		telemetry.addLine("Cycle Transfer: LEFT BUMPER");
		telemetry.addLine("Cycle Intake: RIGHT BUMPER");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Increment Turret Position: DPAD-UP");
		telemetry.addLine("Decrement Turret Position: DPAD-DOWN");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Set Turret Position 0: DPAD-LEFT");
		telemetry.addLine("Set Turret Position 1: DPAD-RIGHT");
		telemetry.addLine("---------------------------------------------");
		telemetry.addData("Intake Speed:", robot.getIntake().getSpeed());
		telemetry.addData("Launcher Speed:", robot.getLauncher().getPower());
		telemetry.addData("Launcher Hood Position", robot.getLauncherHood().getServoPos());
		telemetry.update();
	}

}
