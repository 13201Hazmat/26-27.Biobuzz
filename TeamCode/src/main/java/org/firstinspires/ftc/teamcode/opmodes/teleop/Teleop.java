package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Main")
public abstract class Teleop extends BaseTeleop {
	public Teleop(HazmatRobot robot) {
		super(robot);

		gp1.leftStickButton().onTrue(robot.getIntake().toggleRamp());
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));
	}

	@Override
	public void start() {
		robot.getDrivetrain().startDrive(gamepad1, robot.getHeading());
	}

	@Override
	public void periodic() {
	}
}
