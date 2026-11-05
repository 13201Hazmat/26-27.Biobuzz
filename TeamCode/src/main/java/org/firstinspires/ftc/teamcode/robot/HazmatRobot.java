package org.firstinspires.ftc.teamcode.robot;

import com.pedropathing.follower.Follower;
import com.pedropathing.paths.Path;
import com.skeletonarmy.marrow.weaver.PathResult;
import com.skeletonarmy.marrow.weaver.PedroPathingConverter;
import com.skeletonarmy.marrow.zones.Zone;

import org.firstinspires.ftc.teamcode.mechanisms.Drivetrain;
//import org.firstinspires.ftc.teamcode.mechanisms.vision.Limelight;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.hivevisionftc.vision.Detection;
import org.hivevisionftc.vision.Pose;
import org.hivevisionftc.vision.weaver.WeaverRoute;

import java.util.List;
import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private Drivetrain drivetrain;
//	private Limelight limelight;

	public Drivetrain getDrivetrain() {
		return drivetrain;
	}

//	public Limelight getLimelight() {
//		return limelight;
//	}

	public Follower getFollower() {
		if (follower == null) {
			follower = Constants.create(RobotController.hardwareMap());
		}
		return follower;
	}

	public Pose convertMarrowToPose(com.pedropathing.math.Pose pose) {
		return new Pose(pose.x(), pose.y(), pose.heading());
	}

//	public List<Detection> getDetections() {
//		return limelight.vision.fetchDetections(convertMarrowToPose(getFollower().pose()));
//	}

//	public Path getPathToBall(int amtBalls, Zone... zone) {
//		PathResult result = WeaverRoute.from(convertMarrowToPose(getFollower().pose()))
//				.avoid(zone)
//				.maxTargets(amtBalls)
//				.plan(getDetections());
//
//		if (result == null) {
//			return null;
//		}
//		return PedroPathingConverter.toPath(result);
//	}

	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(drivetrain);
	}
}