package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import com.skeletonarmy.marrow.prompts.BooleanPrompt;
import com.skeletonarmy.marrow.prompts.OptionPrompt;
import com.skeletonarmy.marrow.prompts.Prompter;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runnables;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Back Auto")
public class LateShootAuto extends BaseAuto {
	private final Prompter prompter;

	public LateShootAuto(HazmatRobot robot) {
		super(robot, Alliance.RED);
		robot.init();
		this.startingPose = runnables.paths.backLeaveAutoPoses.start;
		setAutoCommand(runnables.backLeaveAuto(() -> false));

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
				"Back Leave Auto",
				"Back Far Flower Auto"
			)
		);

		prompter.prompt(
			"isFront", new BooleanPrompt("Start Front?", true)
		).label("Is Front");

		prompter.showSummary().onComplete(this::onPromptsComplete);
	}

	public void onPromptsComplete() {
		Alliance alliance = prompter.get("alliance");
		Alliance.currentAlliance = alliance;

		this.runnables = new Runnables(robot);
		this.startingPose = runnables.paths.backLeaveAutoPoses.start;

		String autoSelection = prompter.get("autoSelection");

		if ("Back Leave Auto".equals(autoSelection)) {
			setAutoCommand(runnables.backLeaveAuto(() -> prompter.getOrDefault("isFront", true)));
		} else if ("Back Far Flower Auto".equals(autoSelection)) {
			setAutoCommand(runnables.backFarFlowerAuto(() -> prompter.getOrDefault("isFront", true)));
		}

		telemetry.addData("Selected Alliance", alliance);
		telemetry.addData("Selected Auto", autoSelection);
		telemetry.addData("isFront", prompter.getOrDefault("isFront", true));
		telemetry.update();
	}

	@Override
	public void disabledPeriodic() {
		prompter.run();
	}
}