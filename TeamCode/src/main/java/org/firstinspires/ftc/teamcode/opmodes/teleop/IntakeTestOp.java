package org.firstinspires.ftc.teamcode.opmodes.teleop;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Intake Test Teleop")
public class IntakeTestOp extends NextOpMode {
	private final HazmatRobot robot;
	public IntakeTestOp(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		Trigger.Companion.getDefaultEventLoop().clear();
		CommandGamepad gp1 = new CommandGamepad(gamepad1);
		gp1.rightBumper().onTrue(robot.getIntake().setForward());
		gp1.leftBumper().onTrue(robot.getIntake().setReverse());
		gp1.cross().onTrue(robot.getIntake().setOff());
		gp1.dpadUp().onTrue(robot.getIntake().upPower());
		gp1.dpadDown().onTrue(robot.getIntake().downPower());
	}

	@Override
	public void periodic() {

	}
}
