package org.firstinspires.ftc.teamcode.robot;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.onInterrupt;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.TurretPoses;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherHood;
import org.firstinspires.ftc.teamcode.mechanisms.transfer.Transfer;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.ArrayList;
import java.util.List;
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
				TurretPoses.targetPose = TurretPoses.redFarHive;
			} else {
				TurretPoses.targetPose = TurretPoses.redAudienceHive;
			}
		}
		if (a == Alliance.BLUE) {
			if (current.y() > 70.5) {
				TurretPoses.targetPose = TurretPoses.blueFarHive;
			} else {
				TurretPoses.targetPose = TurretPoses.blueAudienceHive;
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

	private Command setHoodForFirst() {
		return lazy(() -> {
			BallType front = transfer.getTransferSensing().getTopBall();
			if (front == BallType.POLLEN)
				return launcherHood.setPollenPos();
			if (front == BallType.RED_NECTAR || front == BallType.BLUE_NECTAR)
				return launcherHood.setNectarPos();
			return instant(() -> {
			});
		});
	}

	private Command setHoodFor(BallType type) {
		return conditional(() -> type == BallType.POLLEN, launcherHood.setPollenPos(), launcherHood.setNectarPos());
	}

	private Command cycleHood() {
		return lazy(() -> {
			List<BallType> balls = new ArrayList<>(transfer.getTransferSensing().getStoredBalls());
			if (balls.isEmpty())
				return instant(() -> {
				});
			List<Command> commands = new ArrayList<>();

			commands.add(waitMs(250));
			for (BallType ballType : balls.subList(1, balls.size())) {
				if (ballType == null || ballType == BallType.NOTHING)
					break;
				commands.add(setHoodFor(ballType));
				commands.add(waitMs(150));
			}
			return sequential(commands.toArray(new Command[0]));
		});
	}

	// @formatter:off
	public Command launch() {
		return sequential(
				instant(() -> isLaunching = true),
				setHoodForFirst(),
				waitUntil(launcher::isAtVelocity).raceWith(waitMs(1000)),
				transfer.setForward(),
				launcher.openGate(),
				cycleHood(),
				waitMs(1000),
				launcher.closeGate(),
				instant(() -> isLaunching = false)
		).raceWith(onInterrupt(() -> isLaunching = false))
				.unless(transfer.getTransferSensing().getStoredBalls()::isEmpty);
	}
	// @formatter:on

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