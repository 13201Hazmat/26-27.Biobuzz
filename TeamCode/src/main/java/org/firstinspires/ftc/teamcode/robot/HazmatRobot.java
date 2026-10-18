package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private Intake intake;
	private Turret turret;

	public HazmatRobot() {
		intake = new Intake();
		turret = new Turret(RobotController.hardwareMap());
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
	public double getOmega() {
		return getFollower().velocity().omega;
	}

	public Intake getIntake() {
		return intake;
	}

	public Turret getTurret() {
		return turret;
	}
	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, turret);
	}

	@Override
	public void periodic(){
		getFollower().update();
//		turret.trackGoal(PathsAndPoses.targetPose, getPose());
	}
}