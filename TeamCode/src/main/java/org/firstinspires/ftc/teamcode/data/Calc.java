package org.firstinspires.ftc.teamcode.data;

import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.TurretPoses;

import com.pedropathing.math.Pose;

import dev.nextftc.control.util.InterpolatingMap;

public class Calc {
	public static double distance(Pose current) {
		return Math.hypot(TurretPoses.targetPose.x() - current.x(), TurretPoses.targetPose.y() - current.y()) / 12;
	}

	private static final InterpolatingMap<Double> velocity = buildVelocity();

	private static InterpolatingMap<Double> buildVelocity() {
		InterpolatingMap<Double> map = InterpolatingMap.Companion.spline();
		map.put(0.0, 0.0); // distance, velocity
		map.put(1.0, 0.0);
		map.put(2.0, 0.0);
		return map;
	}

	public static double getVelocity(Pose current) {
		return velocity.get(distance(current));
	}
}
