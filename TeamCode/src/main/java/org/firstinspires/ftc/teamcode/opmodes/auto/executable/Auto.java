package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runnables;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import java.util.ArrayList;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Auto")
public class Auto extends BaseAuto {
	private final Prompter prompter;

	public Auto(HazmatRobot robot) {
		super(robot, Alliance.RED);

		// Fallback default command using BooleanSupplier
		setAutoCommand(runnables.leaveAuto(() -> false));

		this.prompter = new Prompter(telemetry, gamepad1, gamepad2);
//		 robot.getTurret().setAutoAim(true);
		prompter.prompt(
			"alliance", new OptionPrompt<>(
				"Select Alliance",
				Alliance.RED,
				Alliance.BLUE
			)
		);

		prompter.prompt(
			"autoSelection", new OptionPrompt<>(
				"Select Auto Routine",
				"Leave Auto",
				"Preload Auto",
				"Close Flower Auto",
				"Far Flower Auto"
			)
		);

		prompter.prompt(
			"isFront", new BooleanPrompt("Start Front?", true)
		).label("Is Front");

		prompter.prompt(
			"throughMiddle", new BooleanPrompt("Go Through Middle?", false)
		).showIf("autoSelection", "Preload Auto").or("autoSelection", "Close Flower Auto").or(
			"autoSelection", "Far Flower Auto"
		).label("Through Middle");

		prompter.showSummary().onComplete(this::onPromptsComplete);
	}

	public void onPromptsComplete() {
		Alliance alliance = prompter.get("alliance");
		Alliance.currentAlliance = alliance;

		this.runnables = new Runnables(robot);
		this.startingPose = runnables.paths.cp.startingPose;

		String autoSelection = prompter.get("autoSelection");

		if ("Leave Auto".equals(autoSelection)) {
			setAutoCommand(runnables.leaveAuto(() -> prompter.getOrDefault("isFront", true)));
		} else if ("Preload Auto".equals(autoSelection)) {
			setAutoCommand(
				runnables.preloadAuto(
					() -> prompter.getOrDefault("isFront", true),
					() -> prompter.getOrDefault("throughMiddle", false)
				)
			);
		} else if ("Close Flower Auto".equals(autoSelection)) {
			setAutoCommand(
				runnables.closeFlowerAuto(
					() -> prompter.getOrDefault("isFront", true),
					() -> prompter.getOrDefault("throughMiddle", false)
				)
			);
		} else if ("Far Flower Auto".equals(autoSelection)) {
			setAutoCommand(
				runnables.farFlowerAuto(
					() -> prompter.getOrDefault("isFront", true),
					() -> prompter.getOrDefault("throughMiddle", false)
				)
			);
		}

		telemetry.addData("Selected Alliance", alliance);
		telemetry.addData("Selected Auto", autoSelection);
		telemetry.addData("isFront", prompter.getOrDefault("isFront", true));
		if (!"Leave Auto".equals(autoSelection)) {
			telemetry.addData("throughMiddle", prompter.getOrDefault("throughMiddle", false));
		}
		telemetry.update();
	}

	@Override
	public void disabledPeriodic() {
		robot.init().schedule();
		prompter.run();
	}

	@Override
	public void start() {
		super.start();
		robot.getTransfer().getTransferSensing().getStoredBalls().add(BallType.POLLEN);
		robot.getTransfer().getTransferSensing().getStoredBalls().add(BallType.POLLEN);
		robot.getTransfer().getTransferSensing().getStoredBalls().add(BallType.POLLEN);
		robot.getTransfer().getTransferSensing().getStoredBalls().add(BallType.POLLEN);
	}

	@Override
	public void periodic() {
		telemetry.addData("QUEU", robot.getTransfer().getTransferSensing().getStoredBalls());
		telemetry.addData("STATE", robot.getTransfer().getTransferSensingState());
		telemetry.update();
	}
}