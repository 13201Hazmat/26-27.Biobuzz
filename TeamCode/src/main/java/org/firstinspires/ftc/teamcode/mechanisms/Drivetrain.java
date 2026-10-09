package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.follower.Follower;
import dev.nextftc.robot.Mechanism;

public class Drivetrain implements Mechanism {
	private final Follower follower;

	public Drivetrain(Follower follower) {
		this.follower = follower;
	}

	public Follower getFollower() {
		return follower;
	}

	@Override
	public void periodic() {
		if (follower != null) {
			follower.update();
		}
	}
}
