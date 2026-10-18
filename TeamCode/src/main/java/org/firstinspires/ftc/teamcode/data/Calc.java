package org.firstinspires.ftc.teamcode.data;

import com.pedropathing.math.Pose;

public class Calc {
    private static double TURRET_OFFSET_IN = 0.0;

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
}