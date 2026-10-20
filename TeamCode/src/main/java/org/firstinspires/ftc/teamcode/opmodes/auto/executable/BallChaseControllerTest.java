/*
 * BallChaseControllerTest - TeleOp harness for the no-odometry
 * BallChaseController tool. Use this to validate everything before trusting it
 * in auto:
 *
 *   Right bumper (HOLD) : run the chase. Releasing it aborts instantly and
 *                         returns manual control.
 *   D-pad in init       : pick which ball classes to chase (UP = red,
 *                         DOWN = blue, LEFT = red+blue default, RIGHT = all).
 *   Left stick / right stick X : manual drive.  Left bumper : intake.
 *
 * The auto equivalent is just this loop body: create the controller, start(),
 * update() each loop until isDone(), abort() when done.
 */
package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;
import org.firstinspires.ftc.teamcode.mechanisms.vision.BallChaseController;
import org.firstinspires.ftc.teamcode.mechanisms.vision.BallTracker;

@TeleOp(name = "Ball Chase Tool Test", group = "Hive Vision")
public class BallChaseControllerTest extends LinearOpMode {

	private static final int PIPELINE_INDEX = 0;
	private HazmatRobot robot;
	@Override
	public void runOpMode() {
		robot = new HazmatRobot();

		BallTracker tracker = new BallTracker(robot.getVision().getLimelight());
		BallChaseController chase = new BallChaseController(tracker, robot.getDrivetrain(), null);
		chase.setMaxPickups(3);

		while (!isStarted() && !isStopRequested()) {
			if (gamepad1.dpad_up)
				tracker.setAllowedClasses(BallTracker.CLASS_RED);
			if (gamepad1.dpad_down)
				tracker.setAllowedClasses(BallTracker.CLASS_BLUE);
			if (gamepad1.dpad_left)
				tracker.setAllowedClasses(BallTracker.CLASSES_RED_BLUE);
			if (gamepad1.dpad_right)
				tracker.setAllowedClasses(BallTracker.CLASSES_ALL);

			telemetry.addLine("Hold RIGHT BUMPER to chase; release = manual.");
			telemetry.addLine("Init: UP=red DOWN=blue LEFT=red+blue RIGHT=all");
			telemetry.addData("chasing",
					BallTracker.class.getSimpleName() + " classes: " + tracker.getAllowedClasses());
			telemetry.update();
		}

		waitForStart();

		boolean wasHeld = false;
		while (opModeIsActive()) {
			boolean held = gamepad1.right_bumper;
			if (held && !wasHeld)
				chase.start();
			if (!held && wasHeld)
				chase.abort();
			wasHeld = held;

			if (held) {
				chase.update();
			} else {
				robot.getDrivetrain().startDrive(gamepad1, 90);
			}

			chase.addTelemetry(telemetry);
			telemetry.update();
		}

		chase.abort();
		robot.getVision().getLimelight().stop();
	}
}