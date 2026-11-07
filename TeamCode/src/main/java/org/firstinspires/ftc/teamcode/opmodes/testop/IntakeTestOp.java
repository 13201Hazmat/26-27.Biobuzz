package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Intake Test Teleop")
public class IntakeTestOp extends NextOpMode {
	private final HazmatRobot robot;
	public IntakeTestOp(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setManualTransferTrue();
		robot.setUtilityOpMode(true);


		Trigger.Companion.getDefaultEventLoop().clear();
		CommandGamepad gp1 = new CommandGamepad(gamepad1);
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));

		gp1.dpadUp().onTrue(robot.getIntake().increasePower());
		gp1.dpadDown().onTrue(robot.getIntake().decreasePower());

		gp1.dpadRight().onTrue(robot.getIntake().setRampUp());
		gp1.dpadLeft().onTrue(robot.getIntake().setRampDown());

		gp1.dpadLeft().onTrue(instant(() -> robot.getIntake().setRampPosition(0.0)));
		gp1.dpadRight().onTrue(instant(() -> robot.getIntake().setRampPosition(1.0)));
	}

	@Override
	public void periodic() {
		telemetry.addLine("---TELEMETRY---");
		telemetry.addLine("Cycle Intake: RIGHT BUMPER");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Increase Intake Speed: DPAD-UP");
		telemetry.addLine("Decrease Intake Speed: DPAD-DOWN");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Increase Intake Ramp Position: DPAD-RIGHT");
		telemetry.addLine("Decrease Intake Ramp Position: DPAD-LEFT");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Set Intake Ramp Position 0: DPAD-LEFT");
		telemetry.addLine("Set Intake Ramp Position 1: DPAD-RIGHT");
		telemetry.addLine("---------------------------------------------");
		telemetry.addData("Intake Speed:", robot.getIntake().getSpeed());
		telemetry.addData("Intake Ramp Position:", robot.getIntake().getPosition());
		telemetry.update();
	}
}
