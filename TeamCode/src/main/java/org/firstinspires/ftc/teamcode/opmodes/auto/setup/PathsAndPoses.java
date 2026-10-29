package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
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

	public static class TurretPoses {
		public static Pose targetPose = factory.of(0, 0, 0);

		public static Pose redAudienceHive = factory.of(58, 53, 0);
		public static Pose redFarHive = factory.of(58, 88, 0);
		public static Pose blueAudienceHive = factory.of(84, 53, 0);
		public static Pose blueFarHive = factory.of(84, 88, 0);
	}

	public static class LeavePoses {
		public static Pose startingPose = factory.of(30.43, 7.48, 180);
		public static Pose midPose = factory.of(9.24, 7.30, 180);
		public static Pose control = factory.of(25.96, 56.11, 180);
		public static Pose frontLeavePose = factory.of(10.34, 117.81, 180);
		public static Pose backLeavePose = factory.of(10.11, 95.7, 90);

		// Direct path from start to finish
		public static Path startingPose_To_frontLeavePose = createLinearPath(
				LeavePoses.startingPose,
				LeavePoses.frontLeavePose
		);

		public static Path startingPose_To_backLeavePose = createLinearPath(
				LeavePoses.startingPose,
				LeavePoses.backLeavePose
		);

		public static Path startingPose_To_midPose = createLinearPath(
				LeavePoses.startingPose,
				LeavePoses.midPose
		);
		// Uses control point from the mid point
		public static Path midPose_To_frontLeavePose = createCurvyPath(
				LeavePoses.midPose,
				LeavePoses.control,
				LeavePoses.frontLeavePose
		);
		public static Path midPose_To_backLeavePose = createCurvyPath(
				LeavePoses.midPose,
				LeavePoses.control,
				LeavePoses.backLeavePose
		);
	}

	public static class BasicFlowerAutoPoses {
		public static Pose startingPose = LeavePoses.startingPose;
		public static Pose midPose = factory.of(9.24, 7.30, 180);
		public static Pose control = factory.of(21.36, 86.38, 90);
		public static Pose midControl = factory.of(95.96, 51.82, 90);
		public static Pose shootPose = factory.of(47.16, 123, 90);
		public static Pose endFlowerPose = factory.of(47.16, 129, 90);
		public static Pose frontLeavePose = factory.of(10.34, 117.81, 90);
		public static Pose backLeavePose = factory.of(10.11, 95.7, 90);

		public static Path startingPose_To_midPose = createLinearPath(startingPose, midPose);
		public static Path midPose_To_shootPose_MID = createCurvyPath(midPose, midControl, shootPose);
		public static Path midPose_To_shootPose = createCurvyPath(midPose, control, shootPose);
		public static Path shootPose_To_endFlowerPose = createLinearPath(shootPose, endFlowerPose);
		public static Path endFlowerPose_To_frontLeavePose = createLinearPath(endFlowerPose, frontLeavePose);
		public static Path endFlowerPose_To_backLeavePose = createLinearPath(endFlowerPose, backLeavePose);
	}

	public static class DoubleFlowerAutoPoses {
		public static Pose startingPose = factory.of(30.43, 7.48, 180);
		public static Pose frontLeavePose = factory.of(10.34, 117.81, 180);
		public static Pose backLeavePose = factory.of(10.11, 95.7, 90);
	}
}
