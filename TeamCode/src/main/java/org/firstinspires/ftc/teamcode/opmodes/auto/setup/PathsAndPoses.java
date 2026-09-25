package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

public class PathsAndPoses {

    private static final PoseFactory factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);
    public static Pose currentPose = factory.of(67, 67, 67);
}
