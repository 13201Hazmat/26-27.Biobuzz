package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.telemetry.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Intake Test")
public class IntakeTestOp extends NextOpMode {
	private final HazmatRobot robot;
	public IntakeTestOp(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setManualTransferTrue();
		robot.setActiveOpMode(this);

		Trigger.Companion.getDefaultEventLoop().clear();


	}
	@Override
	public void start(){
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));

		gp1.dpadUp().onTrue(robot.getIntake().increasePower());
		gp1.dpadDown().onTrue(robot.getIntake().decreasePower());

		gp1.dpadRight().onTrue(robot.getIntake().setRampUp());
		gp1.dpadLeft().onTrue(robot.getIntake().setRampDown());

		gp1.dpadLeft().onTrue(instant(() -> robot.getIntake().setRampPosition(0.0)));
		gp1.dpadRight().onTrue(instant(() -> robot.getIntake().setRampPosition(1.0)));

		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));

		gp1.leftTrigger().isOver(0.2).onTrue(instant(()->robot.setDebugEnabled(true))).onFalse(instant(()-> robot.setDebugEnabled(false)));
	}

	@Override
	public void periodic() {
		Telemetry.log("Right Bumper", "Cycle Intake");
		Telemetry.log("DPad Up", "Increase Intake Power");
		Telemetry.log("DPad Down", "Decrease Intake Power");
		Telemetry.log("DPad Right", "Ramp Up / Set Ramp Position (1.0)");
		Telemetry.log("DPad Left", "Ramp Down / Set Ramp Position (0.0)");
		Telemetry.log("Left Bumper", "Cycle Transfer");
		Telemetry.log("Left Trigger", "Debug");


	}
}
