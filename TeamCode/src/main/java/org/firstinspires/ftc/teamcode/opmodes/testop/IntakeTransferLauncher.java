package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Intake Transfer Launcher Test")
public class IntakeTransferLauncher extends NextOpMode {
	private final HazmatRobot robot;
	public IntakeTransferLauncher(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		Trigger.Companion.getDefaultEventLoop().clear();
		CommandGamepad gp1 = new CommandGamepad(gamepad1);
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));
		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));
		gp1.rightTrigger().isOver(0.6).onTrue(instant(() -> robot.getLauncher().test()));
		gp1.dpadUp().onTrue(instant(() -> robot.getLauncher().increasePower()));
		gp1.dpadDown().onTrue(instant(() -> robot.getLauncher().decreasePower()));
		gp1.dpadRight().onTrue(instant(() -> robot.getLauncherHood().incrementUp()));
		gp1.dpadLeft().onTrue(instant(() -> robot.getLauncherHood().incrementDown()));

	}

	@Override
	public void periodic() {
		robot.getIntake().debug();
	}
}
