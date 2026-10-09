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

	@NonNull
	@Override
	public String toString() {
		String finalString = "";
		if (currentBallType == BallType.NOTHING) {
			finalString = "NOTHING";
		} else if (currentBallType == BallType.POLLEN) {
			finalString = "POLLEN";
		} else if (currentBallType == BallType.RED_NECTAR) {
			finalString = "RED NECTAR";
		} else if (currentBallType == BallType.BLUE_NECTAR) {
			finalString = "BLUE NECTAR";
		}
		return finalString;
	}
}