package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Launcher Test")
public class LauncherTest extends NextOpMode {
	private final HazmatRobot robot;

	public LauncherTest(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		Trigger.Companion.getDefaultEventLoop().clear();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.dpadUp().onTrue(instant(() -> robot.getLauncher().increasePower()));
		gp1.dpadDown().onTrue(instant(() -> robot.getLauncher().decreasePower()));

		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));

		// Launcher Hood Controls
		gp1.dpadRight().onTrue(robot.getLauncherHood().incremen(0.01));
		gp1.dpadLeft().onTrue(robot.getLauncherHood().incremen(-0.01));
		gp1.cross().onTrue(robot.getLauncherHood().setServoPos(0.0));
		gp1.triangle().onTrue(robot.getLauncherHood().setServoPos(1.0));

		// Launcher Gate Controls
		gp1.rightTrigger().isOver(0.3).onTrue(robot.getLauncher().incremen(0.01));
		gp1.leftTrigger().isOver(0.3).onTrue(robot.getLauncher().incremen(-0.01));
		gp1.circle().onTrue(robot.getLauncher().openGate());
		gp1.square().onTrue(robot.getLauncher().closeGate());
	}

	@Override
	public void periodic() {
		telemetry.addLine("---TELEMETRY---");
		telemetry.addLine("Cycle Transfer: LEFT BUMPER");
		telemetry.addLine("Cycle Intake: RIGHT BUMPER");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Start Launcher: SQUARE");
		telemetry.addLine("Increase Launcher Speed: DPAD-UP");
		telemetry.addLine("Decrease Launcher Speed: DPAD-DOWN");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Increase Launcher Hood Position: DPAD-RIGHT");
		telemetry.addLine("Decrease Launcher Hood Position: DPAD-LEFT");
		telemetry.addLine("Set Launcher Hood Position 0: CROSS");
		telemetry.addLine("Set Launcher Hood Position 1: TRIANGLE");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Increment Launcher Gate Position: RIGHT TRIGGER");
		telemetry.addLine("Decrement Launcher Gate Position: LEFT TRIGGER");
		telemetry.addData("Intake Speed:", robot.getIntake().getSpeed());
		telemetry.addData("Launcher Speed:", robot.getLauncher().getPower());
		telemetry.addData("Launcher Hood Position", robot.getLauncherHood().getServoPos());
		telemetry.addData("Launcher Gate Position", robot.getLauncher().getLauncherGatePos());
		telemetry.update();
	}

}
