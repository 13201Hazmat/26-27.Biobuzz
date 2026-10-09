package org.firstinspires.ftc.teamcode.data;

import dev.nextftc.hardware.sensors.colors.ColorProfile;
import dev.nextftc.hardware.sensors.colors.ColorSpace;
import dev.nextftc.hardware.sensors.colors.NextColor;

public class ColorProfiles {
	private static final NextColor ACTUAL_POLLEN_COLOR = NextColor.Companion.hsv(88.5f, 0.6f, 0.035f);
	private static final NextColor TOLERANT_POLLEN_COLOR = NextColor.Companion.hsv(96.5f, 1f, 0.055f);
	private static final NextColor ACTUAL_RED_NECTAR_COLOR = NextColor.Companion.hsv(10f, 1f, 0.06f);
	private static final NextColor TOLERANT_RED_NECTAR_COLOR = NextColor.Companion.hsv(50f, 1f, 0.3f);
	private static final NextColor ACTUAL_BLUE_NECTAR_COLOR = NextColor.Companion.hsv(215.5f, 1f, 0.016f);
	private static final NextColor TOLERANT_BLUE_NECTAR_COLOR = NextColor.Companion.hsv(220.5f, 1f, 0.02f);
	public static final ColorProfile POLLEN_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV, ACTUAL_POLLEN_COLOR,
			TOLERANT_POLLEN_COLOR);
	public static final ColorProfile RED_NECTAR_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV,
			ACTUAL_RED_NECTAR_COLOR, TOLERANT_RED_NECTAR_COLOR);
	public static final ColorProfile BLUE_NECTAR_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV,
			ACTUAL_BLUE_NECTAR_COLOR, TOLERANT_BLUE_NECTAR_COLOR);
}
