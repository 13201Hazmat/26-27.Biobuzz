package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.Alliance;

public class PathsAndPoses {

	private final PoseFactory factory;

	private CommonPoints cp = new CommonPoints();
	public final LeavePoses leavePoses;
	public PathsAndPoses() {
		factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);
		if (Alliance.currentAlliance == Alliance.BLUE) {
			factory.mirrorX(70.75);
			factory.mirrorY(70.75);
		}

		leavePoses = new LeavePoses();
	}

	public static Path createLinearPath(Pose p1, Pose p2) {
		return Paths.line(p1, p2).linear(p1, p2);
	}

	public static Path createCurvyPath(Pose p1, Pose control, Pose p2) {
		return Paths.curve(p1, control, p2).linear(p1, p2);
	}

	public static class TurretPoses {
		public static Pose targetPose = new Pose(0, 0, 0);
		public static final Pose redAudienceHive = new Pose(58, 53, 0);
		public static final Pose redFarHive = new Pose(58, 88, 0);
		public static final Pose blueAudienceHive = new Pose(84, 53, 0);
		public static final Pose blueFarHive = new Pose(84, 88, 0);
	}

	/* Auto Path Poses */

	public class CommonPoints {
		public final Pose startingPose = factory.of(30.43, 7.48, 180);
		public final Pose gardenIntakePose = factory.of(9.24, 7.31, 180);
		public final Pose frontLeavePose = factory.of(10.34, 117.81, 0);
		public final Pose backLeavePose = factory.of(9.53, 99.19, 0);
	}

	/* Starting at Open Hive */
	public class LeavePoses {
		public MidLeavePoses midLeavePoses;
		public final Pose startingPose = cp.startingPose;
		public final Pose midPose = cp.gardenIntakePose;
		public final Pose control = factory.of(25.96, 56.11, 180);
		public final Pose middleControl = factory.of(86.68, 32.16, 90);
		public final Pose midLeavePose = factory.of(72.37, 103.32, 90);
		public final Pose frontLeavePose = cp.frontLeavePose;
		public final Pose backLeavePose = cp.backLeavePose;

		public final Path startingPose_To_frontLeavePose = createLinearPath(startingPose, frontLeavePose);
		public final Path startingPose_To_backLeavePose = createLinearPath(startingPose, backLeavePose);
		public final Path startingPose_To_midPose = createLinearPath(startingPose, midPose);
		public final Path midPose_To_frontLeavePose = createCurvyPath(midPose, control, frontLeavePose);
		public final Path midPose_To_backLeavePose = createCurvyPath(midPose, control, backLeavePose);

		public class MidLeavePoses {
			public final Pose startingPose = cp.startingPose;
			public final Pose midPose = cp.gardenIntakePose;
			public final Pose middleControl = factory.of(86.68, 32.16, 90);
			public final Pose midLeavePose = factory.of(72.37, 103.32, 90);
			public final Pose frontLeavePose = cp.frontLeavePose;
			public final Pose backLeavePose = cp.backLeavePose;

			public final Path midPose_To_midLeavePose = createCurvyPath(midPose, middleControl, midLeavePose);
			public final Path midLeavePose_To_frontLeavePose = createLinearPath(midLeavePose, frontLeavePose);
			public final Path midLeavePose_To_backLeavePose = createLinearPath(midLeavePose, backLeavePose);
		}
	}
}