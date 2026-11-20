package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Calc;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;
import org.opencv.core.Mat;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Regression Tuning")
public class Regression extends NextOpMode {
	private final HazmatRobot robot;

	public Regression(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setActiveOpMode(this);
		robot.init();
		Trigger.Companion.getDefaultEventLoop().clear();

	}

	@Override
	public void start() {
		PoseFactory factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);
		CommandGamepad gp1 = new CommandGamepad(gamepad1);
		robot.getDrivetrain().startDrive(gamepad1);

		gp1.triangle().onTrue(instant(() -> robot.getLauncher().incrementRPM()));
		gp1.cross().onTrue(instant(() -> robot.getLauncher().decrementRPM()));
		gp1.circle().onTrue(instant(() -> robot.getLauncher().setTargetVelocity(0)));
		gp1.square().onTrue(instant(() -> robot.setPose(factory.of(133.5,7.8, 90))));


		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));
		gp1.rightBumper().onTrue(instant(() -> robot.getTransfer().cycle()));
		gp1.leftBumper().onTrue(instant(()->robot.getLauncher().autoSetVelocity(robot.getPose())));
		gp1.rightTrigger().isOver(0.2).onTrue(robot.launch());
	}

	@Override
	public void periodic() {
		robot.getLauncher().PID();
		telemetry.addData("Target RPM", robot.getLauncher().getTargetVelocity());
		telemetry.addData("Current RPM", robot.getLauncher().getCurrentRPM());
		telemetry.addData("Error", robot.getLauncher().getTargetVelocity() - robot.getLauncher().getCurrentRPM());
		telemetry.addData("Odod", robot.getPose());
		telemetry.addData("Distance", Calc.distance(robot.getPose()));
	}
}
