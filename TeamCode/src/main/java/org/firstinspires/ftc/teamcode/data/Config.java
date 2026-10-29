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

	/* CONTROL HUB SERVOS */

	public static NextLynxModule launcherGateServoModule = RobotController.controlHub();
	public static int launcherGateServoPort = 0;

	public static NextLynxModule intakeRampServoModule = RobotController.controlHub();
	public static int intakeRampServoPort = 1;
	/* EXPANSION HUB MOTORS */

	public static NextLynxModule transferMotorModule = RobotController.expansionHub();
	public static int transferMotorPort = 0;
	public static NextLynxModule leftLauncherModule = RobotController.expansionHub();
	public static int leftLauncherPort = 1;
	public static NextLynxModule rightLauncherModule = RobotController.expansionHub();
	public static int rightLauncherPort = 2;
	public static NextLynxModule intakeMotorModule = RobotController.expansionHub();
	public static int intakeMotorPort = 3;

	/* EXPANSION HUB SERVOS */

}