package org.firstinspires.ftc.teamcode.data;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;

import java.util.Arrays;
import java.util.List;

import dev.nextftc.control.util.InterpolatingMap;
import dev.nextftc.control.util.InterpolatingMap2D;

public class Calc {
    public static double distance(Pose current){
        return Math.hypot(PathsAndPoses.targetPose.x() - current.x(), PathsAndPoses.targetPose.y() - current.y())/12;
    }

    private static final InterpolatingMap<Double> velocity = buildVelocity();

    private static InterpolatingMap<Double> buildVelocity() {
        InterpolatingMap<Double> map = InterpolatingMap.Companion.spline();
        map.put(0.0, 0.0); // distance, velocity
        map.put(1.0, 0.0);
        map.put(2.0, 0.0);
        return map;
    }

    public static double getVelocity(Pose current){
        return velocity.get(distance(current));
    }
}
