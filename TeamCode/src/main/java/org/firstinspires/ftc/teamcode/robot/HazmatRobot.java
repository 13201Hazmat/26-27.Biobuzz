package org.firstinspires.ftc.teamcode.robot;

import static com.pedropathing.ivy.groups.Groups.parallel;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherHood;
import org.firstinspires.ftc.teamcode.mechanisms.transfer.Transfer;

import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.Telemetry;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private final Drivetrain drivetrain;
	private final Intake intake;
	private final Transfer transfer;
	private final Launcher launcher;
	private final LauncherHood launcherHood;

	public HazmatRobot() {
		drivetrain = new Drivetrain();
		intake = new Intake();
		transfer = new Transfer();
		launcher = new Launcher();
		launcherHood = new LauncherHood();
	}

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	public Drivetrain getDrivetrain() {
		return drivetrain;
	}
	public Intake getIntake() {
		return intake;
	}
	public Transfer getTransfer() {
		return transfer;
	}
	public Launcher getLauncher() {
		return launcher;
	}
	public LauncherHood getLauncherHood() {
		return launcherHood;
	}



	public Pose getCurrentPose() {
		return follower.pose();
	}

	public double getHeading() {
		return follower.pose().heading();
	}



	public void setTargetPose(Alliance a, Pose current) {
		if (a == Alliance.RED) {
			if (current.y() > 70.5) {
				PathsAndPoses.targetPose = PathsAndPoses.redFarHive;
			} else {
				PathsAndPoses.targetPose = PathsAndPoses.redAudienceHive;
			}
		}
		if (a == Alliance.BLUE) {
			if (current.y() > 70.5) {
				PathsAndPoses.targetPose = PathsAndPoses.blueFarHive;
			} else {
				PathsAndPoses.targetPose = PathsAndPoses.blueAudienceHive;
			}
		}
	}



	@NonNull
	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, transfer, drivetrain, launcher, launcherHood);
	}


	public void updateTelemetry() {
		transfer.debug();
		drivetrain.debug();
		intake.debug();
		launcher.debug();
		launcherHood.debug();
		Telemetry.update();
	}

	public Command init() {
		return parallel(
				intake.setRampDown()
		);
	}

	@Override
	public void periodic() {

	}
}