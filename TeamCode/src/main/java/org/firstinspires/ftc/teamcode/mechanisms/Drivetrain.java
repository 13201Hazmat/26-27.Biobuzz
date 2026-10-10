package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.drive.DriveCommands;

public class Drivetrain implements Mechanism {
	public final NextMotor frontLeft = new NextMotor("frontLeft");
	public final NextMotor frontRight = new NextMotor("frontRight");
	public final NextMotor backLeft = new NextMotor("backLeft");
	public final NextMotor backRight = new NextMotor("backRight");

	public String debug() {
		return "FL - " + frontLeft.getThrottle() + "\nFR - " + frontRight.getThrottle() + "\nBL - "
				+ backLeft.getThrottle() + "\nBR" + backRight.getThrottle();

	}

	public void startDrive(Gamepad gamepad) {
		DriveCommands.mecanumDrive(frontLeft, frontRight, backLeft, backRight, gamepad).schedule();
	}
}