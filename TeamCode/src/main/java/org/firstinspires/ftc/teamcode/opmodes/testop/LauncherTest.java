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
		gp1.triangle().onTrue(instant(() -> robot.getLauncher().test()));

		gp1.dpadRight().onTrue(instant(() -> robot.getLauncherHood().incrementUp()));
		gp1.dpadRight().onTrue(instant(() -> robot.getLauncherHood().incrementDown()));

	}

	@Override
	public void periodic() {
		robot.updateTelemetry();
	}

}
