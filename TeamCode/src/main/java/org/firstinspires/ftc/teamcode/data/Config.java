package org.firstinspires.ftc.teamcode.data;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.lynx.NextLynxModule;

public class Config {

	// Have to use strings because we are using pedro
	public static String fl = "frontLeft";
	public static String fR = "frontRight";
	public static String bL = "backLeft";
	public static String bR = "backRight";

	/* CONTROL HUB MOTORS */
	public static NextLynxModule leftLauncherModule = RobotController.controlHub();
	public static int leftLauncherPort = 1;
	public static NextLynxModule rightLauncherModule = RobotController.controlHub();
	public static int rightLauncherPort = 2;

	/* EXPANSION HUB MOTORS */
	public static NextLynxModule intakeMotorModule = RobotController.expansionHub();
	public static int intakeMotorPort = 1;
	public static NextLynxModule transferMotorModule = RobotController.expansionHub();
	public static int transferMotorPort = 2;

	/* CONTROL HUB SERVOS */
	public static NextLynxModule launcherBackPlateServoModule = RobotController.controlHub();
	public static int launcherBackPlateServoPort = 0;
	public static NextLynxModule leftIntakeRampServo = RobotController.controlHub();
	public static int intakeRampServoPort = 2;

	public static NextLynxModule launcherGateServoModule = RobotController.controlHub();
	public static int launcherGateServoPort = 5;

	/* EXPANSION HUB SERVOS */
	public static NextLynxModule leftTurretModule = RobotController.expansionHub();
	public static int leftTurretPort = 0;
	public static NextLynxModule rightIntakeRampModule = RobotController.expansionHub();
	public static int rightIntakeRampPort = 2;
	public static NextLynxModule rightTurretModule = RobotController.expansionHub();
	public static int rightTurretPort = 3;

	/* CONTROL HUB DIGITAL SENSORS */
	public static NextLynxModule intakeBeamBreakModule = RobotController.controlHub();
	public static int intakeBeamBreakPort = 3;

	public static NextLynxModule launcherBeamBreakModule = RobotController.controlHub();
	public static int launcherBeamBreakPort = 5;

	// right launcher hood servo
//	public static NextLynxModule flowerHoodServoModule = RobotController.expansionHub();
//	public static int flowerHoodServoPort = 4;

}