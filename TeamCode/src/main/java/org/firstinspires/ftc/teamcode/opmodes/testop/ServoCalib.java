package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Servo Calibration")
public class ServoCalib extends NextOpMode {

	private final HazmatRobot robot;
	private int selectedIndex = 0;
	private final double STEP = 0.01;

	private final String[] servoNames = {
		"Launcher Gate",
		"Launcher Hood",
		"Intake Ramp",
		"Turret Servos"
	};

	public ServoCalib(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setManualTransferTrue();
		robot.setActiveOpMode(this);

		Trigger.Companion.getDefaultEventLoop().clear();
		CommandGamepad gp1 = new CommandGamepad(gamepad1);

		gp1.rightBumper().onTrue(instant(() -> {
			selectedIndex = (selectedIndex + 1) % servoNames.length;
		}));
		gp1.leftBumper().onTrue(instant(() -> {
			selectedIndex = (selectedIndex - 1 + servoNames.length) % servoNames.length;
		}));

		gp1.dpadUp().onTrue(instant(() -> adjustSelected(STEP)));
		gp1.dpadDown().onTrue(instant(() -> adjustSelected(-STEP)));

		gp1.cross().onTrue(instant(() -> setSelectedPos(0.0)));
		gp1.square().onTrue(instant(() -> setSelectedPos(0.5)));
		gp1.triangle().onTrue(instant(() -> setSelectedPos(1.0)));
	}

	private void adjustSelected(double delta) {
		switch (selectedIndex) {
			case 0 :
				robot.getLauncher().incremen(delta).schedule();
				break;
			case 1 :
				robot.getLauncherHood().increment(delta).schedule();
				break;
			case 2 :
				robot.getIntake().incremen(delta).schedule();
				break;
			case 3 :
				robot.getTurret().incremen(delta).schedule();
				break;
		}
	}

	private void setSelectedPos(double pos) {
		switch (selectedIndex) {
			case 0 :
				robot.getLauncher().incremen(pos - robot.getLauncher().getLauncherGatePos()).schedule();
				break;
			case 1 :
				robot.getLauncherHood().setServoPos(pos).schedule();
				break;
			case 2 :
				robot.getIntake().setRampPosition(pos);
				break;
			case 3 :
				robot.getTurret().setPositions(pos);
				break;
		}
	}

	private double getSelectedPos() {
		switch (selectedIndex) {
			case 0 :
				return robot.getLauncher().getLauncherGatePos();
			case 1 :
				return robot.getLauncherHood().getServoPos();
			case 2 :
				return robot.getIntake().getPosition();
			case 3 :
				return robot.getTurret().getAngle();
			default :
				return 0.0;
		}
	}

	@Override
	public void periodic() {
		telemetry.addLine("=== SERVO CALIBRATION ===");
		telemetry.addLine("Controls:");
		telemetry.addLine("  LB / RB       : Select Servo");
		telemetry.addLine("  DPAD UP / DOWN: Increment / Decrement Position");
		telemetry.addLine("  CROSS / TRI   : Preset 0.0 / 1.0");
		telemetry.addLine("  SQUARE        : Preset 0.5");
		telemetry.addLine("---------------------------------------------");
		telemetry.addData(
			"SELECTED SERVO",
			String.format("[%d/%d] %s", selectedIndex + 1, servoNames.length, servoNames[selectedIndex])
		);
		telemetry.addData("SELECTED POSITION", String.format("%.4f", getSelectedPos()));
		telemetry.addLine("---------------------------------------------");
		telemetry.addData("1. Launcher Gate", String.format("%.4f", robot.getLauncher().getLauncherGatePos()));
		telemetry.addData("2. Launcher Hood", String.format("%.4f", robot.getLauncherHood().getServoPos()));
		telemetry.addData("3. Intake Ramp  ", String.format("%.4f", robot.getIntake().getPosition()));
		telemetry.addData("4. Turret Servos", String.format("%.4f", robot.getTurret().getAngle()));
		telemetry.update();
	}
}
