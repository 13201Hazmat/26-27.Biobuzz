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

	public Drivetrain() {
		frontLeft.setDirection(NextMotor.Direction.REVERSE);
		frontRight.setDirection(NextMotor.Direction.FORWARD);
		backLeft.setDirection(NextMotor.Direction.FORWARD);
		backRight.setDirection(NextMotor.Direction.FORWARD);
	}

	public void debug() {
		Telemetry.log("FL -", frontLeft.getThrottle());
		Telemetry.log("FR -", frontRight.getThrottle());
		Telemetry.log("BL -", backLeft.getThrottle());
		Telemetry.log("BR -", backRight.getThrottle());
	}

	public void startDrive(Gamepad gamepad, double heading) {
		DriveCommands.mecanumDriveFieldCentric(frontLeft, frontRight, backLeft, backRight, gamepad, () -> heading)
				.schedule();
	}
}