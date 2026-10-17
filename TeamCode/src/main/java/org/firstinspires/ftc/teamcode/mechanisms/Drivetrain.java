package org.firstinspires.ftc.teamcode.mechanisms;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;
import dev.nextftc.robot.drive.DriveCommands;
import dev.nextftc.robot.opmode.NextTeleop;

public class Drivetrain implements Mechanism {
	public final NextMotor frontLeft = new NextMotor(Config.fl);
	public final NextMotor frontRight = new NextMotor(Config.fR);
	public final NextMotor backLeft = new NextMotor(Config.bL);
	public final NextMotor backRight = new NextMotor(Config.bR);

	public Drivetrain() {

		frontLeft.setDirection(NextMotor.Direction.REVERSE);
		frontRight.setDirection(NextMotor.Direction.FORWARD);
		backLeft.setDirection(NextMotor.Direction.REVERSE);
		backRight.setDirection(NextMotor.Direction.FORWARD);

	}

	public void debug() {
		Telemetry.log("FL -", frontLeft.getThrottle());
		Telemetry.log("FR -", frontRight.getThrottle());
		Telemetry.log("BL -", backLeft.getThrottle());
		Telemetry.log("BR -", backRight.getThrottle());
	}

	public void startDrive(Gamepad gamepad, double heading) {
		DriveCommands.mecanumDriveFieldCentric(frontLeft, frontRight, backLeft, backRight, gamepad, () -> heading).schedule();
	}
}