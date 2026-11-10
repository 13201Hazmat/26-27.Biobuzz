package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextTeleop;

@NextTeleop(name = "Main")
public class TeleOp extends BaseTeleop {
	public TeleOp(HazmatRobot robot, Alliance red) {
		super(robot);

		gp1.leftStickButton().onTrue(robot.getIntake().toggleRamp());
		gp1.rightBumper().onTrue(instant(() -> robot.getIntake().cycle()));
	}

	@Override
	public void start() {
		robot.getDrivetrain().startDrive(gamepad1);
	}

	@Override
	public void periodic() {
	}
}
