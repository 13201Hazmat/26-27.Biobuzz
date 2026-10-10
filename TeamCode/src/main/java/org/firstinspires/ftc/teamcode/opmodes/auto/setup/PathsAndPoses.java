package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

public class PathsAndPoses {

	private static final PoseFactory factory = new PoseFactory(PoseFactory.Operation.IDENTITY, true);
	public static Pose currentPose = factory.of(67, 67, 67);

	public static Pose targetPose = factory.of(0,0,0);

	public static Pose redAudienceHive = factory.of(0,0,0);
	public static Pose redFarHive = factory.of(0,0,0);
	public static Pose blueAudienceHive = factory.of(0,0,0);
	public static Pose blueFarHive = factory.of(0,0,0);



}
