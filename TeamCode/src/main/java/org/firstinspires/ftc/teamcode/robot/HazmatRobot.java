package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.LauncherHood;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private Intake intake;
	private Launcher launcher;
	private LauncherHood launcherHood;

	public HazmatRobot() {
		intake = new Intake();
		launcher = new Launcher();
		launcherHood = new LauncherHood();
	}

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	public Pose getCurrentPose(){
		return getFollower().pose();
	}

	public Intake getIntake() {
		return intake;
	}
	public Launcher getLauncher() {
		return launcher;
	}

	public void setTargetPose(Alliance a, Pose current){
		if (a == Alliance.RED){
			if (current.y() > 70.5){
				PathsAndPoses.targetPose = PathsAndPoses.redFarHive;
			} else {
				PathsAndPoses.targetPose = PathsAndPoses.redAudienceHive;
			}
		}
		if (a == Alliance.BLUE){
			if (current.y() > 70.5){
				PathsAndPoses.targetPose = PathsAndPoses.blueFarHive;
			} else {
				PathsAndPoses.targetPose = PathsAndPoses.blueAudienceHive;
			}
		}
	}

	public void updateTelemetry(Telemetry telemetry){
		telemetry.addData("Launcher", launcher.debug());
		telemetry.addData("Launcher Hood", launcherHood.debug());
	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake);
	}
}