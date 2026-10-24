package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

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

    public static Path createCurvyPath(Pose p1, Pose control, Pose p2) {
        return Paths.curve(p1, control, p2).linear(p1, p2);
    }

    public static class LeavePoses {
        public static final Pose startingPose = factory.of(30.43, 7.48, 180);
        public static final Pose midPose = factory.of(9.24, 7.30, 180);
        public static final Pose control = factory.of(25.96, 56.11, 180);
        public static final Pose leavePose = factory.of(9.76, 96.28, 180);
    }

    public static class HazPaths {
        // Direct path from start to finish
        public static Path startingPose_To_leavePose = createLinearPath(LeavePoses.startingPose, LeavePoses.leavePose);
        public static Path startingPose_To_midPose = createLinearPath(LeavePoses.startingPose, LeavePoses.midPose);
        // Uses control point from the mid point
        public static Path midPose_To_leavePose = createCurvyPath(LeavePoses.midPose, LeavePoses.control, LeavePoses.leavePose);
    }
}
