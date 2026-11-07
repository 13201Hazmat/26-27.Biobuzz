package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.match;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import java.util.EnumMap;

import dev.nextftc.telemetry.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Servo Calibration")
public class ServoCalib extends NextOpMode {
	private final HazmatRobot robot;

	private enum Selected { INTAKE, TURRET, GATE, HOOD }
	private Selected last = Selected.INTAKE;

	public ServoCalib(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setManualTransferTrue();

		Trigger.Companion.getDefaultEventLoop().clear();
	}

	private Command goTo(double target) {
		EnumMap<Selected, Command> cases = new EnumMap<>(Selected.class);
		cases.put(Selected.INTAKE, instant(() ->
				robot.getIntake().increment(target - robot.getIntake().getPosition()).schedule()));
		cases.put(Selected.TURRET, instant(() ->
				robot.getTurret().increment(target - robot.getTurret().getRightTurretServoPos()).schedule()));
		cases.put(Selected.GATE, instant(() ->
				robot.getLauncher().increment(target - robot.getLauncher().getLauncherGatePos()).schedule()));
		cases.put(Selected.HOOD, instant(() ->
				robot.getLauncherHood().increment(target - robot.getLauncherHood().getServoPos()).schedule()));
		return match(() -> last, cases);
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.circle().onTrue(robot.getIntake().increment(0.01));
		gp1.circle().onTrue(instant(() -> last = Selected.INTAKE));
		gp1.square().onTrue(robot.getIntake().increment(-0.01));
		gp1.square().onTrue(instant(() -> last = Selected.INTAKE));

		gp1.triangle().onTrue(robot.getTurret().increment(0.01));
		gp1.triangle().onTrue(instant(() -> last = Selected.TURRET));
		gp1.cross().onTrue(robot.getTurret().increment(-0.01));
		gp1.cross().onTrue(instant(() -> last = Selected.TURRET));

		gp1.dpadUp().onTrue(robot.getLauncher().increment(0.01));
		gp1.dpadUp().onTrue(instant(() -> last = Selected.GATE));
		gp1.dpadDown().onTrue(robot.getLauncher().increment(-0.01));
		gp1.dpadDown().onTrue(instant(() -> last = Selected.GATE));

		gp1.dpadRight().onTrue(robot.getLauncherHood().increment(0.01));
		gp1.dpadRight().onTrue(instant(() -> last = Selected.HOOD));
		gp1.dpadLeft().onTrue(robot.getLauncherHood().increment(-0.01));
		gp1.dpadLeft().onTrue(instant(() -> last = Selected.HOOD));

		gp1.leftBumper().onTrue(goTo(0));
		gp1.rightBumper().onTrue(goTo(1));
	}

	@Override
	public void periodic() {
		Telemetry.log("Selected Servo", last);
		Telemetry.log("Left Bumper / Right Bumper", "Selected servo to 0 / 1");

		Telemetry.log("Intake Ramp Position", robot.getIntake().getPosition());
		Telemetry.log("Circle / Square", "Intake +0.01 / -0.01");

		Telemetry.log("Right Turret Servo Position", robot.getTurret().getRightTurretServoPos());
		Telemetry.log("Left Turret Servo Position", robot.getTurret().getLeftTurretServoPos());
		Telemetry.log("Triangle / Cross", "Turret +0.01 / -0.01");

		Telemetry.log("Launcher Gate Position", robot.getLauncher().getLauncherGatePos());
		Telemetry.log("DPad Up / Down", "Launcher Gate +0.01 / -0.01");

		Telemetry.log("Launcher Hood Position", robot.getLauncherHood().getServoPos());
		Telemetry.log("DPad Right / Left", "Hood +0.01 / -0.01");
	}
}