package org.firstinspires.ftc.teamcode.data;

import dev.nextftc.hardware.sensors.colors.ColorProfile;
import dev.nextftc.hardware.sensors.colors.ColorSpace;
import dev.nextftc.hardware.sensors.colors.NextColor;

public class ColorProfiles {
	private static final NextColor ACTUAL_POLLEN_COLOR = NextColor.Companion.hsv(88.5f, 0.6f, 0.035f);
	private static final NextColor POLLEN_TOLERANCE = NextColor.Companion.hsv(20f, 0.4f, 0.2f);

	private static final NextColor ACTUAL_RED_NECTAR_COLOR = NextColor.Companion.hsv(10f, 1f, 0.06f);
	private static final NextColor RED_NECTAR_TOLERANCE = NextColor.Companion.hsv(20f, 0.4f, 0.3f);

	private static final NextColor ACTUAL_BLUE_NECTAR_COLOR = NextColor.Companion.hsv(215.5f, 1f, 0.016f);
	private static final NextColor BLUE_NECTAR_TOLERANCE = NextColor.Companion.hsv(20f, 0.4f, 0.2f);

	public static final ColorProfile POLLEN_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV, ACTUAL_POLLEN_COLOR,
			POLLEN_TOLERANCE);
	public static final ColorProfile RED_NECTAR_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV,
			ACTUAL_RED_NECTAR_COLOR, RED_NECTAR_TOLERANCE);
	public static final ColorProfile BLUE_NECTAR_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV,
			ACTUAL_BLUE_NECTAR_COLOR, BLUE_NECTAR_TOLERANCE);
}
