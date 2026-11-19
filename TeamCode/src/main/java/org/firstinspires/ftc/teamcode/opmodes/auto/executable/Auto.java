package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runnables;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Auto")
public class Auto extends BaseAuto {
	private final Prompter prompter;

	public Auto(HazmatRobot robot) {
		super(robot, Alliance.RED);

		// Fallback default command using BooleanSupplier
		setAutoCommand(runnables.leaveAuto(() -> false));

		this.prompter = new Prompter(telemetry, gamepad1, gamepad2);

		prompter.prompt(
			"alliance", new OptionPrompt<>(
				"Select Alliance",
				Alliance.RED,
				Alliance.BLUE
			)
		);

		prompter.prompt(
			"startPosition", new OptionPrompt<>(
				"Select Starting Position",
				"Front",
				"Back"
			)
		).label("Starting Position");

		prompter.prompt(
			"autoSelectionFront", new OptionPrompt<>(
				"Select Auto Routine",
				"Leave Auto",
				"Preload Auto",
				"Close Flower Auto",
				"Far Flower Auto",
				"Double Flower Auto"
			)
		).showIf("startPosition", "Front").label("Auto Routine");

		prompter.prompt(
			"autoSelectionBack", new OptionPrompt<>(
				"Select Auto Routine",
				"Back Leave Auto",
				"Back Far Flower Auto"
			)
		).showIf("startPosition", "Back").label("Auto Routine");

		prompter.prompt(
			"isFront", new BooleanPrompt("Leave Pose: Front (YES) or Back (NO)?", true)
		).label("Leave Pose");

		prompter.prompt(
			"throughMiddle", new BooleanPrompt("Go Through Middle?", false)
		).showIf("autoSelectionFront", "Preload Auto")
		 .or("autoSelectionFront", "Close Flower Auto")
		 .or("autoSelectionFront", "Far Flower Auto")
		 .or("autoSelectionFront", "Double Flower Auto")
		 .label("Through Middle");

		prompter.showSummary().onComplete(this::onPromptsComplete);
	}

	public void onPromptsComplete() {
		Alliance alliance = prompter.get("alliance");
		Alliance.currentAlliance = alliance;

		this.runnables = new Runnables(robot);

		String startPosition = prompter.get("startPosition");

		if ("Front".equals(startPosition)) {
			this.startingPose = runnables.paths.cp.startingPose;
			String autoSelection = prompter.get("autoSelectionFront");

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
			} else if ("Double Flower Auto".equals(autoSelection)) {
				setAutoCommand(
					runnables.doubleFlowerAuto(
						() -> prompter.getOrDefault("isFront", true),
						() -> prompter.getOrDefault("throughMiddle", false)
					)
				);
			}

			telemetry.addData("Selected Alliance", alliance);
			telemetry.addData("Starting Position", startPosition);
			telemetry.addData("Selected Auto", autoSelection);
			telemetry.addData("Leave Pose (Front)", prompter.getOrDefault("isFront", true));
			if (!"Leave Auto".equals(autoSelection)) {
				telemetry.addData("throughMiddle", prompter.getOrDefault("throughMiddle", false));
			}
		} else { // "Back"
			this.startingPose = runnables.paths.backLeaveAutoPoses.start;
			String autoSelection = prompter.get("autoSelectionBack");

			if ("Back Leave Auto".equals(autoSelection)) {
				setAutoCommand(runnables.backLeaveAuto(() -> prompter.getOrDefault("isFront", true)));
			} else if ("Back Far Flower Auto".equals(autoSelection)) {
				setAutoCommand(runnables.backFarFlowerAuto(() -> prompter.getOrDefault("isFront", true)));
			}

			telemetry.addData("Selected Alliance", alliance);
			telemetry.addData("Starting Position", startPosition);
			telemetry.addData("Selected Auto", autoSelection);
			telemetry.addData("Leave Pose (Front)", prompter.getOrDefault("isFront", true));
		}

		telemetry.update();
	}

	@Override
	public void disabledPeriodic() {
		prompter.run();
	}
}