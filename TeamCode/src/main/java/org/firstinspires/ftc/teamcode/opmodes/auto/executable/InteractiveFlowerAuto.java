package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runnables;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Interactive Flower Auto")
@Disabled
public class InteractiveFlowerAuto extends BaseAuto {

	private Alliance alliance = Alliance.RED;
	private boolean inMiddle = false;
	private boolean inFrontPark = true;

	private boolean lastX = false;
	private boolean lastY = false;
	private boolean lastB = false;

	public InteractiveFlowerAuto(HazmatRobot robot) {
		super(robot, Alliance.RED);
		updateConfiguration();
	}

	private void updateConfiguration() {
		Alliance.currentAlliance = alliance;
		runnables = new Runnables(robot);
		startingPose = runnables.paths.basicFlowerAutoPoses.startingPose;
		setAutoCommand(runnables.basicFlowerAuto(inMiddle, inFrontPark));
	}

	@Override
	public void disabledPeriodic() {
		boolean currentX = gamepad1.x;
		boolean currentY = gamepad1.y;
		boolean currentB = gamepad1.b;

		boolean pressedX = currentX && !lastX;
		boolean pressedY = currentY && !lastY;
		boolean pressedB = currentB && !lastB;

		lastX = currentX;
		lastY = currentY;
		lastB = currentB;

		boolean changed = false;

		if (pressedX) {
			alliance = (alliance == Alliance.RED) ? Alliance.BLUE : Alliance.RED;
			changed = true;
		}

		if (pressedY) {
			inFrontPark = !inFrontPark;
			changed = true;
		}

		if (pressedB) {
			inMiddle = !inMiddle;
			changed = true;
		}

		if (changed) {
			updateConfiguration();
		}

		telemetry.addLine("=== INTERACTIVE FLOWER AUTO CONFIG ===");
		telemetry.addLine("Controls:");
		telemetry.addLine("  X / Square    : Toggle Alliance (Red/Blue)");
		telemetry.addLine("  Y / Triangle  : Toggle Parking (Front/Back)");
		telemetry.addLine("  B / Circle    : Toggle Shoot Path (Middle/Side)");
		telemetry.addLine("");
		telemetry.addData("ALLIANCE", alliance);
		telemetry.addData("SHOOT PATH", inMiddle ? "MIDDLE PATH" : "SIDE PATH");
		telemetry.addData("PARKING", inFrontPark ? "FRONT PARK" : "BACK PARK");
		telemetry.addData("START POSE", startingPose);
		telemetry.addLine("");
		telemetry.addLine("Press START when ready.");
		telemetry.update();
	}
}
