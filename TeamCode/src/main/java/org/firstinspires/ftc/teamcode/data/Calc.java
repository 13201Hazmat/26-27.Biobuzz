package org.firstinspires.ftc.teamcode.data;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Velocity;

import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;

import dev.nextftc.control.util.InterpolatingMap;

public class Calc {
	private static double TURRET_OFFSET_IN = 0.0;
	private static final InterpolatingMap<Double> velocity = buildVelocity();

	private static InterpolatingMap<Double> buildVelocity() {
		InterpolatingMap<Double> map = InterpolatingMap.Companion.spline();
		map.put(2.517, 4250.00); // distance, velocity
		map.put(3.0153, 4300.00);
		map.put(3.30, 4450.00);
		map.put(3.66, 4600.00);
		map.put(4.08,4575.000);
		map.put(4.65,4750.00);
		map.put(5.28,4825.00);
		map.put(5.8, 5225.00);
		map.put(6.2,5350.00);
		return map;
	}

	public static double getVelocity(Pose current) {
		return velocity.get(distance(current));
	}

	public static double distance(Pose current) {
		return Math.hypot(
			PathsAndPoses.TurretPoses.blueAudienceHive.x() - current.x(),
			PathsAndPoses.TurretPoses.blueAudienceHive.y() - current.y()
		) / 12;
	}

	public static double turretKinematics(Pose g, Pose r) {
		double h = r.heading();
		double tx = r.x() + TURRET_OFFSET_IN * Math.cos(h);
		double ty = r.y() + TURRET_OFFSET_IN * Math.sin(h);
		double fieldAngle = Math.atan2(g.y() - ty, g.x() - tx);
		return (Math.toDegrees(fieldAngle - h));
	}

	public static Pose predict(Pose r, Velocity velo, double lead) {
		return new Pose(r.x() + velo.vx * lead, r.y() + velo.vy * lead, r.heading() + velo.omega * lead);
	}

	public static double clamp(double value, double lo, double hi) {
		return Math.max(lo, Math.min(hi, value));
	}

}
