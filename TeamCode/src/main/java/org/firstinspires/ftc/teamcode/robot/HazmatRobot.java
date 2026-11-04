package org.firstinspires.ftc.teamcode.robot;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.onInterrupt;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.TurretPoses;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherHood;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.mechanisms.transfer.Transfer;

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
	private Intake intake;
	private final Transfer transfer;
	private Turret turret;
	private final Launcher launcher;
	private final LauncherHood launcherHood;

	private boolean isLaunching = false;
	private boolean isHoodAutomate = true;
	private boolean isManualTransfer = false;

	public HazmatRobot() {
		drivetrain = new Drivetrain();
		intake = new Intake();
		try {
			turret = new Turret(RobotController.hardwareMap());
		} catch (Throwable t) {
			turret = null;
		}
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
			try {
				turret = new Turret(RobotController.hardwareMap());
			} catch (Throwable t) {
				turret = null;
			}
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

	@NonNull
	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, transfer, drivetrain, launcher, launcherHood);
	}

	public void toggleHoodAutomate() {
		isHoodAutomate = !isHoodAutomate;
	}

	public void toggleManualTransfer() {
		isManualTransfer = !isManualTransfer;
	}

	public void setManualTransferTrue() {
		isManualTransfer = true;
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
			conditional(() -> !isHoodAutomate, setHoodForFirst(), instant(() -> {
			})),
			waitUntil(launcher::isAtVelocity).raceWith(waitMs(1000)),
			transfer.setForward(),
			launcher.openGate(),
			waitMs(200),
			conditional(() -> !isHoodAutomate, cycleHood(), instant(() -> {
			})),
			waitMs(1000),
			launcher.closeGate(),
			instant(() -> isLaunching = false)
		).raceWith(onInterrupt(() -> {
			isLaunching = false;
			launcher.closeGate().schedule();
		})).unless(() -> transfer.getTransferSensing().getStoredBalls().isEmpty());
	}

	public void updateTelemetry() {
		transfer.debug();
		drivetrain.debug();
		intake.debug();
		launcher.debug();
		launcherHood.debug();
		turret.debug();
		Telemetry.update();
	}

	public void updateStates() {
		if (isLaunching)
			return;
		if (launcher.isPowerMode())
			return;

		switch (transfer.getTransferSensingState()) {
			case EMPTY :
				launcher.setIdle();
				break;
			case FILLING :
			case FULL :
				break;
		}

		if (isManualTransfer)
			return;

		switch (transfer.getTransferSensingState()) {
			case EMPTY :
			case FILLING :
				intake.forward();
				transfer.forward();
				break;
			case FULL :
				intake.off();
				transfer.off();
				break;
		}
	}

	@Override
	public void periodic() {
		updateStates();
		updateTelemetry();
	}
}