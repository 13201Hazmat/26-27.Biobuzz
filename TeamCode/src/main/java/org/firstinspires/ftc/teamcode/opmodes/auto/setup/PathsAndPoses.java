package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.Alliance;

public class PathsAndPoses {

	private final PoseFactory factory;

	public final LeavePoses leavePoses;
	public final BasicFlowerAutoPoses basicFlowerAutoPoses;
	public final DoubleFlowerAutoPoses doubleFlowerAutoPoses;

	public PathsAndPoses() {
		factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);
		if (Alliance.currentAlliance == Alliance.BLUE) {
			factory.mirrorX(70.75);
			factory.mirrorY(70.75);
		}

		leavePoses = new LeavePoses();
		basicFlowerAutoPoses = new BasicFlowerAutoPoses();
		doubleFlowerAutoPoses = new DoubleFlowerAutoPoses();
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

	public class LeavePoses {
		public final Pose startingPose = factory.of(30.43, 7.48, 180);
		public final Pose midPose = factory.of(9.24, 7.30, 180);
		public final Pose control = factory.of(25.96, 56.11, 180);
		public final Pose frontLeavePose = factory.of(10.34, 117.81, 180);
		public final Pose backLeavePose = factory.of(10.11, 95.7, 90);

		public final Path startingPose_To_frontLeavePose = createLinearPath(startingPose, frontLeavePose);
		public final Path startingPose_To_backLeavePose = createLinearPath(startingPose, backLeavePose);
		public final Path startingPose_To_midPose = createLinearPath(startingPose, midPose);
		public final Path midPose_To_frontLeavePose = createCurvyPath(midPose, control, frontLeavePose);
		public final Path midPose_To_backLeavePose = createCurvyPath(midPose, control, backLeavePose);
	}

	public class BasicFlowerAutoPoses {
		public final Pose startingPose = leavePoses.startingPose;
		public final Pose midPose = factory.of(9.24, 7.30, 180);
		public final Pose control = factory.of(21.36, 86.38, 90);
		public final Pose midControl = factory.of(95.96, 51.82, 90);
		public final Pose shootPose = factory.of(47.16, 123, 90);
		public final Pose endFlowerPose = factory.of(47.16, 129, 90);
		public final Pose frontLeavePose = factory.of(10.34, 117.81, 90);
		public final Pose backLeavePose = factory.of(10.11, 95.7, 90);

		public final Path startingPose_To_midPose = createLinearPath(startingPose, midPose);
		public final Path midPose_To_shootPose_MID = createCurvyPath(midPose, midControl, shootPose);
		public final Path midPose_To_shootPose = createCurvyPath(midPose, control, shootPose);
		public final Path shootPose_To_endFlowerPose = createLinearPath(shootPose, endFlowerPose);
		public final Path endFlowerPose_To_frontLeavePose = createLinearPath(endFlowerPose, frontLeavePose);
		public final Path endFlowerPose_To_backLeavePose = createLinearPath(endFlowerPose, backLeavePose);
	}

	public class DoubleFlowerAutoPoses {
		public final Pose startingPose = factory.of(30.43, 7.48, 180);
		public final Pose frontLeavePose = factory.of(10.34, 117.81, 180);
		public final Pose backLeavePose = factory.of(10.11, 95.7, 90);
	}
}
