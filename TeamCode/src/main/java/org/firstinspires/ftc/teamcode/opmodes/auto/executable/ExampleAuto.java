package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runners;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Example Autonomous")
public class ExampleAuto extends NextOpMode {
	private enum AutoOption {
		LEAVE_AUTO("Leave Auto"), PRELOAD_AUTO("Preload Auto"), AUDIENCE_FLOWER_AUTO("Audience Flower Auto");
		private final String name;

		AutoOption(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}
	}

	private final HazmatRobot robot;
	private final Runners runners;

	private int selectedIndex = 0;
	private boolean isSelected = false;
	private boolean lastDpadUp = false;
	private boolean lastDpadDown = false;
	private boolean lastRightBumper = false;
	private boolean lastLeftBumper = false;

	public ExampleAuto(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		this.runners = new Runners(this.robot);
	}

	@Override
	public void disabledPeriodic() {
		boolean currentDpadUp = gamepad1.dpad_up;
		boolean currentDpadDown = gamepad1.dpad_down;
		boolean currentRightBumper = gamepad1.right_bumper;
		boolean currentLeftBumper = gamepad1.left_bumper;

		boolean dpadUpPressed = currentDpadUp && !lastDpadUp;
		boolean dpadDownPressed = currentDpadDown && !lastDpadDown;
		boolean rightBumperPressed = currentRightBumper && !lastRightBumper;
		boolean leftBumperPressed = currentLeftBumper && !lastLeftBumper;

		lastDpadUp = currentDpadUp;
		lastDpadDown = currentDpadDown;
		lastRightBumper = currentRightBumper;
		lastLeftBumper = currentLeftBumper;

		AutoOption[] options = AutoOption.values();

		if (!isSelected) {
			if (dpadUpPressed) {
				selectedIndex--;
				if (selectedIndex < 0) {
					selectedIndex = options.length - 1;
				}
			}
			if (dpadDownPressed) {
				selectedIndex++;
				if (selectedIndex >= options.length) {
					selectedIndex = 0;
				}
			}
			if (rightBumperPressed) {
				isSelected = true;
			}
		} else {
			if (leftBumperPressed) {
				isSelected = false;
			}
		}

		telemetry.addLine("=== AUTONOMOUS SELECTION ===");
		telemetry.addLine("Controls:");
		telemetry.addLine("  DPAD UP / DOWN : Move cursor");
		telemetry.addLine("  RIGHT BUMPER   : Select option");
		telemetry.addLine("  LEFT BUMPER    : Go back / Deselect");
		telemetry.addLine("");

		if (isSelected) {
			telemetry.addData("STATUS", "CONFIRMED: " + options[selectedIndex].getName());
			telemetry.addLine("Press LEFT BUMPER to change selection.");
		} else {
			telemetry.addData("STATUS", "SELECTING (Highlighted: " + options[selectedIndex].getName() + ")");
		}

		telemetry.addLine("");
		telemetry.addLine("Available Routines:");
		for (int i = 0; i < options.length; i++) {
			String cursor = (i == selectedIndex) ? "> " : "  ";
			String selectionMarker = (isSelected && i == selectedIndex) ? " [SELECTED]" : "";
			telemetry.addLine(cursor + options[i].getName() + selectionMarker);
		}

		telemetry.update();
	}

	@Override
	public void start() {
		AutoOption selectedOption = AutoOption.values()[selectedIndex];

		switch (selectedOption) {
			case LEAVE_AUTO :
				runners.leaveAuto().schedule();
				break;
			case PRELOAD_AUTO :
				runners.preloadAuto().schedule();
				break;
			case AUDIENCE_FLOWER_AUTO :
			default :
				runners.basicFlowerAuto().schedule();
				break;
		}
	}
}
