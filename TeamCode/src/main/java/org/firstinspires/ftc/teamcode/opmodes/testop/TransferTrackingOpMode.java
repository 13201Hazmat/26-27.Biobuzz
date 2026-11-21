package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Transfer Tracking")
public class TransferTrackingOpMode extends NextOpMode {
	private final HazmatRobot robot;

	public TransferTrackingOpMode(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setActiveOpMode(this);
		robot.getLauncher().setBangBangEnabled(false);
		Trigger.Companion.getDefaultEventLoop().clear();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));
		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));

	}

	@Override
	public void periodic() {
		telemetry.addData("BEAM BREAK", robot.getTransfer().getTransferSensing().getIntakeBeamBreak().debug());
		telemetry.update();
	}
}
