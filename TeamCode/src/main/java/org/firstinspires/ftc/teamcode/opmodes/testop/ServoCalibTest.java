package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import java.util.function.Supplier;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Servo Calib Test Opmode")
public class ServoCalibTest extends NextOpMode {
	private final HazmatRobot robot;
	private final Prompter prompter;
	private Command upCommand;
	private Command downCommand;
	private Supplier<Double> getPositionCommand;

	public ServoCalibTest(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		robot.setActiveOpMode(this);

		Trigger.Companion.getDefaultEventLoop().clear();

		this.prompter = new Prompter(telemetry, gamepad1, gamepad2);
		prompter.prompt(
			"servoSelection", new OptionPrompt<>(
				"Select Servo",
				"Intake Ramp Servo",
				"Launcher Gate Servo",
				"Launcher Hood Servo",
				"Turret Servo"
			)
		).showSummary().onComplete(this::onPromptsComplete);
	}

	public void onPromptsComplete() {
		String selectedServo = prompter.get("servoSelection");
		if (selectedServo.equals("Intake Ramp Servo")) {
			upCommand = robot.getIntake().increment(0.01);
			downCommand = robot.getIntake().increment(-0.01);
			getPositionCommand = () -> robot.getIntake().getPosition();
		} else if (selectedServo.equals("Launcher Gate Servo")) {
			upCommand = robot.getLauncher().increment(0.01);
			downCommand = robot.getLauncher().increment(-0.01);
			getPositionCommand = () -> robot.getLauncher().getLauncherGatePos();
		} else if (selectedServo.equals("Launcher Hood Servo")) {
			upCommand = robot.getLauncherHood().increment(0.01);
			downCommand = robot.getLauncherHood().increment(-0.01);
			getPositionCommand = () -> robot.getLauncherHood().getServoPos();
		} else if (selectedServo.equals("Turret Servo")) {
			upCommand = robot.getTurret().increment(0.01);
			downCommand = robot.getTurret().increment(-0.01);
			getPositionCommand = () -> robot.getTurret().getRightTurretServoPos();
		}

		telemetry.addData("Selected Servo", selectedServo);
		telemetry.update();
	}

	@Override
	public void disabledPeriodic() {
		prompter.run();
	}

	@Override
	public void start() {
		CommandGamepad gp1 = new CommandGamepad(gamepad1);
		gp1.dpadUp().onTrue(instant(() -> {
			if (upCommand != null) {
				upCommand.schedule();
			}
		}));
		gp1.dpadDown().onTrue(instant(() -> {
			if (downCommand != null) {
				downCommand.schedule();
			}
		}));
	}

	@Override
	public void periodic() {
		if (getPositionCommand != null) {
			telemetry.addData("SERVO POS", getPositionCommand.get());
		}
		telemetry.update();
	}
}