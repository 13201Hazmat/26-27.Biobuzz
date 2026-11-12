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
import androidx.annotation.Nullable;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherHood;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.mechanisms.transfer.Transfer;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;

public class HazmatRobot implements NextRobot {

	private Follower follower;
	private final Drivetrain drivetrain;
	private Intake intake;
	private final Transfer transfer;
	private Turret turret;
	private final Launcher launcher;
	private final LauncherHood launcherHood;

	private boolean isLaunching = false;
	private boolean isHoodAutomated = true;
	private boolean isTransferManual = false;

	private boolean debugEnabled = false;

	private NextOpMode activeOpMode = null;

	@Nullable
	public NextOpMode getActiveOpMode() {
		return activeOpMode;
	}

	public void setActiveOpMode(@Nullable NextOpMode activeOpMode) {
		this.activeOpMode = activeOpMode;
	}

	public Alliance getAlliance() {
		return Alliance.currentAlliance;
	}

	public HazmatRobot() {
		drivetrain = new Drivetrain();
		intake = new Intake();
		transfer = new Transfer();
		launcher = new Launcher();
		launcherHood = new LauncherHood();
	}

	public Drivetrain getDrivetrain() {
		return drivetrain;
	}

	public Intake getIntake() {
		return intake;
	}

	public Turret getTurret() {
		if (turret == null) {
			turret = new Turret(RobotController.hardwareMap());
		}
		return turret;
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

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	public Pose getPose() {
		return getFollower().pose();
	}

	public Velocity getVelocity() {
		return getFollower().velocity();
	}

	public void setStartingPose(Pose pose) {
		getFollower().setPose(pose);
	}

	@NonNull
	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, transfer, drivetrain, launcher, launcherHood, getTurret());
	}

	public Command moveToPoint(Path path) {
		return follow(getFollower(), path);
	}

	public void toggleHoodAutomate() {
		isHoodAutomated = !isHoodAutomated;
	}

	public void toggleManualTransfer() {
		isTransferManual = !isTransferManual;
	}

	public void setManualTransferTrue() {
		isTransferManual = true;
	}

	public void setDebugEnabled(boolean on) {
		debugEnabled = on;
	}
	public boolean isUtilityOpMode() {
		return activeOpMode != null && activeOpMode.getClass().isAnnotationPresent(NextUtility.class);
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

	public Command init() {
		return parallel(
			intake.setRampDown(),
			launcherHood.setPollenPos(),
			launcher.closeGate()
		);
	}

	private Command setHoodForFirst() {
		return lazy(() -> {
			BallType front = transfer.getTransferSensing().getTopBall();
			return setHoodFor(front);
		});
	}

	private Command setHoodFor(BallType type) {
		return conditional(
			() -> type == BallType.POLLEN,
			launcherHood.setPollenPos(),
			launcherHood.setNectarPos()
		);
	}

	private Command cycleHood() {
		return lazy(() -> {
			List<BallType> balls = new ArrayList<>(transfer.getTransferSensing().getStoredBalls());
			List<Command> commands = new ArrayList<>();

			for (BallType ballType : balls.subList(1, balls.size())) {
				if (ballType == null || ballType == BallType.NOTHING)
					break;
				commands.add(setHoodFor(ballType));
				commands.add(waitMs(150));
			}
			return sequential(commands.toArray(new Command[0]));
		});
	}

	public Command launch() {
		return sequential(
			instant(() -> isLaunching = true),
			conditional(() -> !isHoodAutomated, setHoodForFirst(), instant(() -> {
			})),
			waitUntil(launcher::isAtVelocity).raceWith(waitMs(1000)),
			transfer.setForward(),
			launcher.openGate(),
			waitMs(200),
			conditional(() -> !isHoodAutomated, cycleHood(), instant(() -> {
			})),
			waitMs(1000),
			launcher.closeGate(),
			instant(() -> isLaunching = false)
		).raceWith(onInterrupt(() -> {
			isLaunching = false;
			launcher.closeGate().schedule();
		})).unless(() -> transfer.getTransferSensing().getStoredBalls().isEmpty());
	}

	public void debugAll() {
		transfer.debug();
		drivetrain.debug();
		intake.debug();
		launcher.debug();
		launcherHood.debug();
		getTurret().debug();
	}

	public void updateStates() {
		if (isLaunching || launcher.isPowerMode())
			return;

		switch (transfer.getTransferSensingState()) {
			case EMPTY :
				launcher.setIdle();
				setIntakeAndTransfer(true);
				break;
			case FILLING :
				setIntakeAndTransfer(true);
				break;
			case FULL :
				setIntakeAndTransfer(false);
				break;
		}
	}

	private void setIntakeAndTransfer(boolean on) {
		if (isTransferManual)
			return;

		if (on) {
			intake.forward();
			// if (transfer.isStalling()) {
			transfer.off();
			// } else {
			transfer.forward();
			// }
		} else {
			intake.off();
			transfer.off();
		}
	}

	@Override
	public void periodic() {
		setTargetPose(getAlliance(), getPose());

		if (!isUtilityOpMode()) {
			updateStates();
		}

		if (Turret.AUTO_AIM) {
			getTurret().trackGoal(TurretPoses.targetPose, getPose());
		}

		if (debugEnabled) {
			debugAll();
		}
	}
}