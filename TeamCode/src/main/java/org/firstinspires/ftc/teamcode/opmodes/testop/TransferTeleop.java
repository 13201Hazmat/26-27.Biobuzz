package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Transfer Test")
public class TransferTeleop extends NextOpMode {
	private final HazmatRobot robot;
	public TransferTeleop(HazmatRobot robot) {
		super(robot);
		this.robot = robot;

		robot.setActiveOpMode(this);


		Trigger.Companion.getDefaultEventLoop().clear();
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));

		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));

		gp1.rightTrigger().isOver(0.6).onTrue(instant(() -> robot.getTransfer().getTransferSensing().removeTopBall()));
	}

	@Override
	public void periodic() {
		telemetry.addLine("---TELEMETRY---");
		telemetry.addLine("Cycle Intake: RIGHT BUMPER");
		telemetry.addLine("Cycle Transfer: LEFT BUMPER");
		telemetry.addLine("---------------------------------------------");
		telemetry.addLine("Remove Top Ball From Queue: RIGHT TRIGGER");
		telemetry.addData("Intake Speed:", robot.getIntake().getSpeed());
		telemetry.addData("Transfer Speed:", robot.getIntake().getSpeed());
		telemetry.addLine("---------------------------------------------");
		telemetry.addData("Current Ball Type", robot.getTransfer().getTransferSensing().getBallType());
		robot.getTransfer().getTransferSensing().debug();
		telemetry.addData("Queue elements:", robot.getTransfer().getTransferSensing().getStoredBalls());
		telemetry.update();
	}
}
