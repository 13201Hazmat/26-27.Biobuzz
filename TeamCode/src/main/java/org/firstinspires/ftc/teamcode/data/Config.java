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
	public static NextLynxModule leftLauncherMotorModule = RobotController.controlHub();
	public static int leftLauncherMotorPort = 1;
	public static NextLynxModule rightLauncherMotorModule = RobotController.controlHub();
	public static int rightLauncherMotorPort = 2;

	/* EXPANSION HUB MOTORS */
	public static NextLynxModule intakeMotorModule = RobotController.expansionHub();
	public static int intakeMotorPort = 1;
	public static NextLynxModule transferMotorModule = RobotController.expansionHub();
	public static int transferMotorPort = 2;

	/* CONTROL HUB SERVOS */
	public static NextLynxModule launcherBackPlateServoModule = RobotController.controlHub();
	public static int launcherBackPlateServoPort = 0;
	public static NextLynxModule leftIntakeRampServoModule = RobotController.controlHub();
	public static int leftIntakeRampServoPort = 2;
	// TODO add leftFlowerHood = 4;
	public static NextLynxModule launcherGateServoModule = RobotController.expansionHub();
	public static int launcherGateServoPort = 5;

	/* EXPANSION HUB SERVOS */
	public static NextLynxModule leftTurretServoModule = RobotController.expansionHub();
	public static int leftTurretServoPort = 0;
	public static NextLynxModule rightIntakeRampServoModule = RobotController.expansionHub();
	public static int rightIntakeRampServoPort = 2;
	public static NextLynxModule rightTurretServoModule = RobotController.expansionHub();
	public static int rightTurretServoPort = 3;
	// TODO add flowerHoodRight=4

	/* CONTROL HUB DIGITAL SENSORS */
	public static NextLynxModule intakeBeamBreakModule = RobotController.controlHub();
	public static int intakeBeamBreakPort = 3;

	public static NextLynxModule launcherBeamBreakModule = RobotController.controlHub();
	public static int launcherBeamBreakPort = 5;

	public static String intakeColorSensorName = "startColorSensor";

}