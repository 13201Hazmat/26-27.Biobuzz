package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Transfer;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private Intake intake;
	private Transfer transfer;

	public HazmatRobot() {
		intake = new Intake();
		transfer = new Transfer();
	}

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	public Intake getIntake() {
		return intake;
	}
	public Transfer getTransfer() {
		return transfer;
	}

	public void printTelemetry(Telemetry telemetry) {
		telemetry.addData("DEBUG:", transfer.getTransferColorSensor().debug());
		telemetry.addData("BEAM BREAK TRIGGERED:", transfer.getTransferBeamBreak().isTriggered());
		telemetry.addData("BEAM BREAK RAW:", transfer.getTransferBeamBreak().getRawState());
		telemetry.addData("BALL TYPE:", BallType.currentBallType);
		telemetry.addData("QUEUE:", transfer.getStoredBalls());
	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, transfer);
	}
}