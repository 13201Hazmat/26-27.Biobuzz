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
	public final DoubleFlowerWithGardenAutoPoses doubleFlowerWithGardenAutoPoses;
	public PathsAndPoses() {
		factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);
		if (Alliance.currentAlliance == Alliance.BLUE) {
			factory.mirrorX(70.75);
			factory.mirrorY(70.75);
		}

		leavePoses = new LeavePoses();
		basicFlowerAutoPoses = new BasicFlowerAutoPoses();
		doubleFlowerAutoPoses = new DoubleFlowerAutoPoses();
		doubleFlowerWithGardenAutoPoses = new DoubleFlowerWithGardenAutoPoses();
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

		public final Path path1 = createLinearPath(startingPose, frontLeavePose);
		public final Path path2 = createLinearPath(startingPose, backLeavePose);
		public final Path path3 = createLinearPath(startingPose, midPose);
		public final Path path4FrontLeavePose = createCurvyPath(midPose, control, frontLeavePose);
		public final Path path4BackLeavePose = createCurvyPath(midPose, control, backLeavePose);
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

		public final Path path1 = createLinearPath(startingPose, midPose);
		public final Path path2 = createCurvyPath(midPose, midControl, shootPose);
		public final Path path3 = createCurvyPath(midPose, control, shootPose);
		public final Path path4 = createLinearPath(shootPose, endFlowerPose);
		public final Path path5FrontLeavePose = createLinearPath(endFlowerPose, frontLeavePose);
		public final Path path5BackLeavePose = createLinearPath(endFlowerPose, backLeavePose);
	}

	public class DoubleFlowerAutoPoses {
		public final Pose startingPose = factory.of(30.43, 7.48, 180);
		public final Pose firstFlowerControlPose = factory.of(50.89, 52.02, 180);
		public final Pose firstFlowerPose = factory.of(11.7, 46.57, 180);
		public final Pose startSecondFlowerPose = factory.of(47.16, 123, 90);
		public final Pose endSecondFlowerPose = factory.of(47.16, 129, 90);
		public final Pose frontLeavePose = factory.of(10.34, 117.81, 180);
		public final Pose backLeavePose = factory.of(10.11, 95.7, 90);

		public final Path path1 = createCurvyPath(startingPose, firstFlowerControlPose, firstFlowerPose);
		public final Path path2 = createLinearPath(firstFlowerPose, startSecondFlowerPose);
		public final Path path3 = createLinearPath(startSecondFlowerPose, endSecondFlowerPose);
		public final Path path4FrontLeavePose = createLinearPath(endSecondFlowerPose, frontLeavePose);
		public final Path path4BackLeavePose = createLinearPath(endSecondFlowerPose, backLeavePose);
	}

	public class DoubleFlowerWithGardenAutoPoses {
		private final Pose start = factory.of(30.43, 7.48, 180);
		private final Pose path1 = factory.of(11.7003, 46.574, 180);
		private final Pose path1Control1 = factory.of(50.8901, 52.021, 0);
		private final Pose point2Start = factory.of(11.7003, 46.574, 180);
		private final Pose point2 = factory.of(47.16, 123, 90);
		private final Pose point3 = factory.of(47.16, 129, 90);
		private final Pose point4Start = factory.of(47.16, 129, 90);
		private final Pose point4 = factory.of(8.6156, 133.0544, 270);
		private final Pose point4Control1 = factory.of(50.6557, 104.8122, 0);
		private final Pose point5 = factory.of(8.2878, 104.5234, 270);
		private final Pose point6Start = factory.of(8.2878, 104.5234, 270);
		private final Pose point6 = factory.of(14.3714, 10.7, 250);
		private final Pose point6Control1 = factory.of(62.2264, 23.5519, 0);
		private final Pose point7Start = factory.of(14.3714, 10.7, 250);
		private final Pose point7 = factory.of(8.7518, 9.1864, 240);

		public Path path1() {
			return Paths.curve(start, path1Control1, path1).linear(start, path1);
		}

		public Path path2() {
			return Paths.line(point2Start, point2).linear(point2Start, point2);
		}

		public Path path3() {
			return Paths.line(point2, point3).constant(point3);
		}

		public Path path4() {
			return Paths.curve(point4Start, point4Control1, point4).linear(point4Start, point4);
		}

		public Path path5() {
			return Paths.line(point4, point5).constant(point5);
		}

		public Path path6() {
			return Paths.curve(point6Start, point6Control1, point6).linear(point6Start, point6);
		}

		public Path path7() {
			return Paths.line(point7Start, point7).linear(point7Start, point7);
		}
	}

}
