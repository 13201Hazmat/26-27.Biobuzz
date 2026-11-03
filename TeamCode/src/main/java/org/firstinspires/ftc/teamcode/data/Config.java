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

	public static NextLynxModule transferMotorModule = RobotController.controlHub();
	public static int transferMotorPort = 1;
	public static NextLynxModule intakeMotorModule = RobotController.controlHub();
	public static int intakeMotorPort = 2;

	/* CONTROL HUB SERVOS */

	public static NextLynxModule launcherGateServoModule = RobotController.controlHub();
	public static int launcherGateServoPort = 0;

	public static NextLynxModule intakeRampServoModule = RobotController.controlHub();
	public static int intakeRampServoPort = 1;

	private static NextLynxModule expansionHub() {
		try {
			return RobotController.expansionHub();
		} catch (Throwable t) {
			return RobotController.controlHub();
		}
	}

	/* EXPANSION HUB MOTORS */
	public static NextLynxModule leftLauncherModule = expansionHub();
	public static int leftLauncherPort = 1;
	public static NextLynxModule rightLauncherModule = expansionHub();
	public static int rightLauncherPort = 2;

	/* EXPANSION HUB SERVOS */

	public static NextLynxModule leftTurretModule = expansionHub();
	public static int leftTurretPort = 1;
	public static NextLynxModule rightTurretModule = expansionHub();
	public static int rightTurretPort = 2;

	public static NextLynxModule launcherHoodServoModule = RobotController.expansionHub();
	public static int launcherHoodServoPort = 5;

}