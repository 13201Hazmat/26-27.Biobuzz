package org.firstinspires.ftc.teamcode.robot;

import static com.pedropathing.ivy.commands.Commands.*;
import static com.pedropathing.ivy.groups.Groups.*;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherHood;
import org.firstinspires.ftc.teamcode.mechanisms.transfer.Transfer;

import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
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

	private boolean isLaunching = false;

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

	public Command init() {
		return parallel(intake.setRampDown());
	}

	public Command moveToPoint(Path path) {
		return follow(follower, path);
	}

	public Command cycleBallTypeServo() {
		return lazy(() -> {
			Queue<BallType> currentBalls = transfer.getTransferSensing().getStoredBalls();
			List<Command> commands = new ArrayList<>();
			for (BallType ballType : currentBalls) {
				if (ballType == BallType.POLLEN) {
					commands.add(launcherHood.setPollenPos());
				} else if (ballType == BallType.RED_NECTAR || ballType == BallType.BLUE_NECTAR) {
					commands.add(launcherHood.setNectarPos());
				}
				commands.add(transfer.setForward());
			}
			return sequential(commands.toArray(new Command[0]));
		});
	}

	public Command launch() {
		return sequential(
				waitUntil(launcher::isAtVelocity).raceWith(waitMs(1000)),
				instant(() -> isLaunching = true),
				transfer.setForward(),
				launcher.openGate(),
				cycleBallTypeServo(),
				waitMs(2000),
				launcher.closeGate(),
				instant(() -> isLaunching = false)
		);
	}

	public void updateTelemetry() {
		transfer.debug();
		drivetrain.debug();
		intake.debug();
		launcher.debug();
		launcherHood.debug();
		Telemetry.update();
	}
	public void updateStates() {
		if (isLaunching)
			return;
		switch (transfer.getTransferSensingState()) {
			case EMPTY :
				launcher.setIdle();
				intake.setForward();
				transfer.setForward();
				break;
			case FILLING :
				transfer.setForward();
				intake.setForward();
				launcher.autoSetVelocity(getCurrentPose());
				break;
			case FULL :
				transfer.setOff();
				intake.setOff();
				break;
		}
	}

	@Override
	public void periodic() {
		updateStates();
		updateTelemetry();
	}
}