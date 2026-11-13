package org.firstinspires.ftc.teamcode.data;

import com.pedropathing.math.Pose;

public class PoseStorage {
	public static Pose endPose = new Pose(0, 0, 0);

	public static Pose get() {
		return endPose;
	}

	public static void save(Pose pose) {
		endPose = pose;
	}
}