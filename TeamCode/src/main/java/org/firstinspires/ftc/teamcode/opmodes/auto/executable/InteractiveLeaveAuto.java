package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runnables;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Interactive Leave Auto")
public class InteractiveLeaveAuto extends BaseAuto {

	private Alliance alliance = Alliance.RED;
	private boolean isFront = true;

	private boolean lastX = false;
	private boolean lastY = false;

	public InteractiveLeaveAuto(HazmatRobot robot) {
		super(robot, Alliance.RED);
		updateConfiguration();
	}

	private void updateConfiguration() {
		Alliance.currentAlliance = alliance;
		runnables = new Runnables(robot);
		startingPose = runnables.pathsAndPoses.leavePoses.startingPose;
		setAutoCommand(runnables.preloadAuto(isFront));
	}

	@Override
	public void disabledPeriodic() {
		boolean currentX = gamepad1.x;
		boolean currentY = gamepad1.y;

		boolean pressedX = currentX && !lastX;
		boolean pressedY = currentY && !lastY;

		lastX = currentX;
		lastY = currentY;

		boolean changed = false;

		if (pressedX) {
			alliance = (alliance == Alliance.RED) ? Alliance.BLUE : Alliance.RED;
			changed = true;
		}

		if (pressedY) {
			isFront = !isFront;
			changed = true;
		}

		if (changed) {
			updateConfiguration();
		}

		telemetry.addLine("=== INTERACTIVE LEAVE AUTO CONFIG ===");
		telemetry.addLine("Controls:");
		telemetry.addLine("  X / Square    : Toggle Alliance (Red/Blue)");
		telemetry.addLine("  Y / Triangle  : Toggle Parking (Front/Back)");
		telemetry.addLine("");
		telemetry.addData("ALLIANCE", alliance);
		telemetry.addData("PARKING", isFront ? "FRONT PARK" : "BACK PARK");
		telemetry.addData("START POSE", startingPose);
		telemetry.addLine("");
		telemetry.addLine("Press START when ready.");
		telemetry.update();
	}
}
