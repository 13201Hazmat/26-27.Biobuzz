package org.firstinspires.ftc.teamcode.opmodes.testop;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Servo Calibration")
public class ServoCalib extends NextOpMode {
	private final HazmatRobot robot;
	public ServoCalib(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setManualTransferTrue();

		Trigger.Companion.getDefaultEventLoop().clear();
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.dpadUp().onTrue(robot.getLauncher().incrementGate());
		gp1.dpadDown().onTrue(robot.getLauncher().decrementGate());

		gp1.dpadRight().onTrue(robot.getLauncherHood().incrementUp());
		gp1.dpadLeft().onTrue(robot.getLauncherHood().incrementDown());

		gp1.rightBumper().onTrue(robot.getIntake().incrementRamp());
		gp1.leftBumper().onTrue(robot.getIntake().decrementRamp());
	}

	@Override
	public void periodic() {
		Telemetry.log("Launcher gate pos", robot.getLauncher().getLauncherGatePos());
		Telemetry.log("Launcher Hood Position", robot.getLauncherHood().getServoPos());
		Telemetry.log("Intake ramp servo position", robot.getIntake().getPosition());
	}
}
