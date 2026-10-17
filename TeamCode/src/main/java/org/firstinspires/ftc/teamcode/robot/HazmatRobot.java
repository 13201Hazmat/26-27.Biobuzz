package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.transfer.Transfer;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Telemetry;

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

	public void printTelemetry() {
		transfer.debug();
		Telemetry.update();
	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, transfer, transfer.getTransferSensing());
	}
}