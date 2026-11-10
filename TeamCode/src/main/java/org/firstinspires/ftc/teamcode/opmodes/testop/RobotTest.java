package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;
import dev.nextftc.telemetry.Telemetry;

@NextUtility(name = "Robot Test")
public class RobotTest extends NextOpMode {
	private final HazmatRobot robot;

	public RobotTest(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setActiveOpMode(this);

		Trigger.Companion.getDefaultEventLoop().clear();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		robot.getDrivetrain().startDrive(gamepad1);

		//Intake and Transfer Controls
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));
		gp1.leftBumper().onTrue(instant(() -> robot.getTransfer().cycle()));

		//Launcher Controls
		gp1.dpadUp().onTrue(instant(() -> robot.getLauncher().increasePower()));
		gp1.dpadDown().onTrue(instant(() -> robot.getLauncher().decreasePower()));


		// Launcher Hood Controls
		gp1.cross().onTrue(robot.getLauncherHood().setPollenPos());
		gp1.triangle().onTrue(robot.getLauncherHood().setNectarPos());

		// Launcher Gate Controls
		gp1.circle().onTrue(robot.getLauncher().openGate());
		gp1.square().onTrue(robot.getLauncher().closeGate());

		gp1.leftTrigger().isOver(0.2).onTrue(instant(()->robot.setDebugEnabled(true))).onFalse(instant(()-> robot.setDebugEnabled(false)));
	}

	@Override
	public void periodic() {
		Telemetry.log("Joysticks", "Drive Robot");
		Telemetry.log("Right Bumper", "Cycle Intake");
		Telemetry.log("Left Bumper", "Cycle Transfer");
		Telemetry.log("DPad Up", "Increase Launcher Power");
		Telemetry.log("DPad Down", "Decrease Launcher Power");
		Telemetry.log("Cross", "Set Hood to Pollen Position");
		Telemetry.log("Triangle", "Set Hood to Nectar Position");
		Telemetry.log("Circle", "Open Launcher Gate");
		Telemetry.log("Square", "Close Launcher Gate");
		Telemetry.log("Left Trigger", "Debug");


	}

}
