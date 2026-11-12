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

		// Fallback default command
		setAutoCommand(runnables.leaveAuto(false));

		this.prompter = new Prompter(telemetry, gamepad1, gamepad2);

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
				"Preload Auto"
			)
		);

		prompter.prompt(
			"isFront", new BooleanPrompt("Start Front?", true)
		).label("Is Front");

		prompter.prompt(
			"throughMiddle", new BooleanPrompt("Go Through Middle?", false)
		).showIf("autoSelection", "Preload Auto").label("Through Middle");

		prompter.showSummary().onComplete(this::onPromptsComplete);
	}

	public void onPromptsComplete() {
		Alliance alliance = prompter.get("alliance");
		Alliance.currentAlliance = alliance;

		this.runnables = new Runnables(robot);
		this.startingPose = runnables.paths.cp.startingPose;

		String autoSelection = prompter.get("autoSelection");
		boolean isFront = prompter.getOrDefault("isFront", true);
		boolean throughMiddle = prompter.getOrDefault("throughMiddle", false);

		if ("Leave Auto".equals(autoSelection)) {
			setAutoCommand(runnables.leaveAuto(isFront));
		} else if ("Preload Auto".equals(autoSelection)) {
			setAutoCommand(runnables.preloadAuto(isFront, throughMiddle));
		}

		telemetry.addData("Selected Alliance", alliance);
		telemetry.addData("Selected Auto", autoSelection);
		telemetry.addData("isFront", isFront);
		if ("Preload Auto".equals(autoSelection)) {
			telemetry.addData("throughMiddle", throughMiddle);
		}
		telemetry.update();
	}

	@Override
	public void disabledPeriodic() {
		prompter.run();
	}
}
