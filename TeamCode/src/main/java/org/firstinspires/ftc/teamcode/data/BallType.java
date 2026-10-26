package org.firstinspires.ftc.teamcode.data;

import androidx.annotation.NonNull;

import dev.nextftc.hardware.sensors.colors.ColorProfile;

public enum BallType {

	RED_NECTAR(ColorProfiles.RED_NECTAR_COLOR_PROFILE), BLUE_NECTAR(ColorProfiles.BLUE_NECTAR_COLOR_PROFILE), POLLEN(
			ColorProfiles.POLLEN_COLOR_PROFILE), NOTHING(null);

	public static BallType currentBallType = BallType.NOTHING;
	private final ColorProfile ballTypeColorProfile;

	BallType(ColorProfile ballTypeColorProfile) {
		this.ballTypeColorProfile = ballTypeColorProfile;
	}

	public ColorProfile getBallTypeColorProfile() {
		return ballTypeColorProfile;
	}

	@NonNull
	@Override
	public String toString() {
		switch (this) {
			case POLLEN :
				return "POLLEN";
			case RED_NECTAR :
				return "RED NECTAR";
			case BLUE_NECTAR :
				return "BLUE NECTAR";
			case NOTHING :
			default :
				return "NOTHING";
		}
	}
}