package org.firstinspires.ftc.teamcode.data;

import dev.nextftc.hardware.sensors.colors.ColorProfile;

public enum BallType {

	RED_NECTAR(ColorProfiles.RED_NECTAR_COLOR_PROFILE), BLUE_NECTAR(ColorProfiles.BLUE_NECTAR_COLOR_PROFILE), POLLEN(
			ColorProfiles.POLLEN_COLOR_PROFILE), NOTHING(null);

	public static BallType currentBallType = BallType.POLLEN;
	private final ColorProfile ballTypeColorProfile;

	BallType(ColorProfile ballTypeColorProfile) {
		this.ballTypeColorProfile = ballTypeColorProfile;
	}
}