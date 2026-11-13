package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.Alliance;

public class PathsAndPoses {

	private final PoseFactory factory;

	public CommonPoints cp = new CommonPoints();
	public final LeavePoses leavePoses;
	public final FarFlowerAutoPoses farFlowerPoses;
	public final CloseFlowerAutoPoses closeFlowerPoses;
	public final DoubleFlowerAutoPoses doubleFlowerPoses;
	public PathsAndPoses() {
		factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);
		if (Alliance.currentAlliance == Alliance.BLUE) {
			factory.mirrorX(70.75);
			factory.mirrorY(70.75);
		}

		leavePoses = new LeavePoses();
		farFlowerPoses = new FarFlowerAutoPoses();
		closeFlowerPoses = new CloseFlowerAutoPoses();
		doubleFlowerPoses = new DoubleFlowerAutoPoses();
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

		public final Pose beforeFarFlowerIntakePose = factory.of(47.16, 123, 90);
		public final Pose afterFarFlowerIntakePose = factory.of(47.16, 129, 90);
	}

	/* Starting at Open Hive */
	public class LeavePoses {
		public MidLeavePoses midLeavePoses;
		public final Pose control = factory.of(25.96, 56.11, 180);
		public final Path startingPose_To_frontLeavePose() {
			return Paths.line(cp.startingPose, cp.frontLeavePose);
		}
		public final Path startingPose_To_backLeavePose() {
			return Paths.line(cp.startingPose, cp.backLeavePose).linear(cp.startingPose, cp.backLeavePose);
		}
		public final Path startingPose_To_midPose() {
			return Paths.line(cp.startingPose, cp.gardenIntakePose).linear(cp.startingPose, cp.gardenIntakePose);
		}
		public final Path midPose_To_frontLeavePose() {
			return Paths.curve(cp.gardenIntakePose, control, cp.frontLeavePose).linear(cp.gardenIntakePose, cp.frontLeavePose);
		}
		public final Path midPose_To_backLeavePose() {
			return Paths.curve(cp.gardenIntakePose, control, cp.backLeavePose);
		}

		public class MidLeavePoses {
			public final Pose middleControl = factory.of(86.68, 32.16, 90);
			public final Pose midLeavePose = factory.of(72.37, 103.32, 90);
			public final Pose frontLeavePose = cp.frontLeavePose;
			public final Pose backLeavePose = cp.backLeavePose;

			public final Path midPose_To_midLeavePose() {
				return Paths.curve(cp.gardenIntakePose, middleControl, midLeavePose).linear(cp.gardenIntakePose, midLeavePose);
			}
			public final Path midLeavePose_To_frontLeavePose() {
				return Paths.line(midLeavePose, frontLeavePose);
			}
			public final Path midLeavePose_To_backLeavePose() {
				return Paths.line(midLeavePose, backLeavePose);
			}
		}
	}

	public class CloseFlowerAutoPoses {

	}

	public class FarFlowerAutoPoses {
		private final Pose controlPoint = factory.of(21.3681, 86.3828, 0);
		public Path startingPose_To_gardenIntakePose() {
			return Paths.line(cp.startingPose, cp.gardenIntakePose).linear(cp.startingPose, cp.gardenIntakePose);
		}

		public Path gardenIntakePose_To_beforeFarFlowerIntakePose() {
			return Paths.curve(cp.gardenIntakePose, controlPoint, cp.beforeFarFlowerIntakePose).linear(
				cp.gardenIntakePose, cp.beforeFarFlowerIntakePose
			);
		}

		public Path beforeFarFlowerIntakePose_To_afterFarFlowerIntakePose() {
			return Paths.line(cp.beforeFarFlowerIntakePose, cp.afterFarFlowerIntakePose).constant(
				cp.afterFarFlowerIntakePose
			);
		}

		public Path afterFarFlowerIntakePose_To_backLeavePose() {
			return Paths.line(cp.afterFarFlowerIntakePose, cp.backLeavePose).linear(
				cp.afterFarFlowerIntakePose, cp.backLeavePose
			);
		}

		public Path afterFarFlowerIntakePose_To_frontLeavePose() {
			return Paths.line(cp.afterFarFlowerIntakePose, cp.frontLeavePose).linear(
				cp.afterFarFlowerIntakePose, cp.frontLeavePose
			);
		}
	}

	public class DoubleFlowerAutoPoses {

	}

}