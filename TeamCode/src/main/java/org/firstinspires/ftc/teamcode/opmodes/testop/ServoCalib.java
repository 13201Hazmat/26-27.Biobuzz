package org.firstinspires.ftc.teamcode.opmodes.testop;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.telemetry.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
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

		gp1.dpadUp().onTrue(robot.getLauncher().increment(0.01));
		gp1.dpadDown().onTrue(robot.getLauncher().setZero());

		gp1.dpadRight().onTrue(robot.getLauncherHood().increment(0.01));
		gp1.dpadLeft().onTrue(robot.getLauncherHood().increment(-0.01));
		gp1.leftStickButton().onTrue(robot.getLauncherHood().setServoPos(0));

		gp1.rightTrigger().isOver(0.3).onTrue(robot.getIntake().increment(0.01));
		gp1.leftTrigger().isOver(0.3).onTrue(robot.getIntake().increment(-0.01));

		gp1.rightBumper().onTrue(robot.getTransfer().setOff());
		gp1.leftBumper().onTrue(robot.getIntake().setOff());

		gp1.square().onTrue(robot.getTurret().incrementTurret());
		gp1.circle().onTrue(robot.getTurret().decrementTurret());
		gp1.triangle().onTrue(robot.getTurret().zeroTurretServo());
	}

	@Override
	public void periodic() {
		Telemetry.log("Launcher gate pos", robot.getLauncher().getLauncherGatePos());
		Telemetry.log("Launcher Hood Position", robot.getLauncherHood().getServoPos());
		Telemetry.log("Intake ramp servo position", robot.getIntake().getPosition());
	}
}