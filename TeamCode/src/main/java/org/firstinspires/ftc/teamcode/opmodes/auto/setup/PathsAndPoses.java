package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.Poses.*;

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

	static class Poses {
		public static final Pose startingPose = factory.of(55.65, 9.03, 90);
		public static final Pose leavePose = factory.of(9.59, 89.65, 90);

	}

	public static class HazPaths {
		public static Path parkPath = Paths.line(startingPose, leavePose).constant(startingPose);
	}
}
