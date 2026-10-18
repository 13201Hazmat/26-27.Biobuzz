package org.firstinspires.ftc.teamcode.robot;

import static com.pedropathing.ivy.groups.Groups.parallel;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
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
	private Drivetrain drivetrain;

	public HazmatRobot() {
		intake = new Intake();
		transfer = new Transfer();
		drivetrain = new Drivetrain();
	}

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	public double getHeading() {
		return getFollower().pose().heading();
	}

	public Intake getIntake() {
		return intake;
	}
	public Transfer getTransfer() {
		return transfer;
	}

	public void updateTelemetry() {
		transfer.debug();
		drivetrain.debug();
		intake.debug();
		Telemetry.update();
	}

	public Drivetrain getDrivetrain() {
		return drivetrain;
	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, transfer, drivetrain);
	}

	public Command init() {
		return parallel(intake.setRampUp());
	}

}