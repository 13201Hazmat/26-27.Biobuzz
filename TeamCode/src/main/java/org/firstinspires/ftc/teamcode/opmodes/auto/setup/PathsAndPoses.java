package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.Poses.firstFlowerPose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.Poses.leavePose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.Poses.otherSideShootingPose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.Poses.startingPose;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.Alliance;

public class PathsAndPoses {

    private static PoseFactory factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);

    // ALL POSES ARE IN RED
    public static void mirror() {
        if (Alliance.currentAlliance == Alliance.BLUE) {
            factory = factory.mirrorX(70.75);
            factory = factory.mirrorY(70.75);
        }
    }

    public static Path createLinearPath(Pose p1, Pose p2) {
        return Paths.line(p1, p2).linear(p1, p2);
    }

    static class Poses {
        public static final Pose startingPose = factory.of(55.65, 9.03, 90);
        public static final Pose leavePose = factory.of(9.59, 89.65, 90);
        public static final Pose firstFlowerPose = factory.of(15.17, 47.14, -180);
        public static final Pose otherSideShootingPose = factory.of(46.88, 127.49, 90);
    }

    static class ControlPoints {

    }

    public static class HazPaths {
        public static Path startingPose_To_leavePose = createLinearPath(startingPose, leavePose);
        public static Path startingPose_To_firstFlowerPose = createLinearPath(startingPose, firstFlowerPose);

        public static Path firstFlowerPose_To_otherSideShootingPose = createLinearPath(firstFlowerPose,
                otherSideShootingPose);
    }
}
