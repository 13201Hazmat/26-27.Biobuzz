package org.firstinspires.ftc.teamcode.data;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;

import java.util.Arrays;
import java.util.List;

import dev.nextftc.control.util.InterpolatingMap;

public class Calc {
    private static double TURRET_OFFSET_IN = 0.0;


    public static double clamp(double value, double lo, double hi) {
        return Math.max(lo, Math.min(hi, value));
    }

	public static double distance(Pose current) {
		return Math.hypot(
			PathsAndPoses.TurretPoses.targetPose.x() - current.x(),
			PathsAndPoses.TurretPoses.targetPose.y() - current.y()
		) / 12;
	}

	private static final InterpolatingMap<Double> velocity = buildVelocity();

	private static InterpolatingMap<Double> buildVelocity() {
		InterpolatingMap<Double> map = InterpolatingMap.Companion.spline();
		map.put(0.0, 0.0); // distance, velocity
		map.put(1.0, 0.0);
		map.put(2.0, 0.0);
		return map;
	}

    /**
     * Computes the angle the turret must point at to face a goal, relative to the robot heading.
     * <p>
     * The turret pivot position is found by offsetting the robot pose by
     * {@code TURRET_OFFSET_IN} along the robot heading. The field angle from that pivot to the
     * goal is then taken with {@code atan2}, and the robot heading is subtracted.
     *
     * @param g goal pose (only x and y are used), in inches
     * @param r robot pose, with heading in radians
     * @return aim angle relative to the robot heading, in degrees
     */
    public static double turretKinematics(Pose g, Pose r) {
        double h = r.heading();
        double tx = r.x() + TURRET_OFFSET_IN * Math.cos(h);
        double ty = r.y() + TURRET_OFFSET_IN * Math.sin(h);
        double fieldAngle = Math.atan2(g.y() - ty, g.x() - tx);
        return (Math.toDegrees(fieldAngle - h));
    }

	public static double getVelocity(Pose current) {
		return velocity.get(distance(current));
	}


}
