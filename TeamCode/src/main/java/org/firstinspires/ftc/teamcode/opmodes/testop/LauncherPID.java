package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Launcher PID")
public class LauncherPID extends NextOpMode {
	private final HazmatRobot robot;

	public LauncherPID(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		Trigger.Companion.getDefaultEventLoop().clear();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.dpadUp().onTrue(instant(() -> robot.getLauncher().increaseKS()));
		gp1.dpadDown().onTrue(instant(() -> robot.getLauncher().decreaseKS()));
		gp1.dpadRight().onTrue(instant(() -> robot.getLauncher().increaseKP()));
		gp1.dpadLeft().onTrue(instant(() -> robot.getLauncher().decreaseKP()));

		gp1.y().onTrue(instant(() -> robot.getLauncher().incrementRPM()));
		gp1.a().onTrue(instant(() -> robot.getLauncher().decrementRPM()));
		gp1.b().onTrue(instant(() -> robot.getLauncher().setTargetVelocity(0)));
	}

	@Override
	public void periodic() {
		telemetry.addData("Constants", robot.getLauncher().PID());
		telemetry.addData("Target RPM", robot.getLauncher().getTargetVelocity());
		telemetry.addData("Current RPM", robot.getLauncher().getCurrentRPM());
		telemetry.addData("Error", robot.getLauncher().getTargetVelocity() - robot.getLauncher().getCurrentRPM());
		telemetry.addData("Power", robot.getLauncher().getPower());
	}
}
