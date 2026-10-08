package org.firstinspires.ftc.teamcode.data;

import dev.nextftc.hardware.sensors.colors.ColorProfile;
import dev.nextftc.hardware.sensors.colors.ColorSpace;
import dev.nextftc.hardware.sensors.colors.NextColor;

public class ColorProfiles {
	// TODO CHANGE THE COLORS TO CORRESPONDING COLORS
	public static final NextColor ACTUAL_POLLEN_COLOR = new NextColor(1, 1, 1);
	public static final NextColor ACTUAL_RED_NECTAR_COLOR = new NextColor(1, 1, 1);
	public static final NextColor ACTUAL_BLUE_NECTAR_COLOR = new NextColor(1, 1, 1);
	public static final NextColor TOLERANT_POLLEN_COLOR = new NextColor(1, 1, 1);
	public static final NextColor TOLERANT_RED_NECTAR_COLOR = new NextColor(1, 1, 1);
	public static final NextColor TOLERANT_BLUE_NECTAR_COLOR = new NextColor(1, 1, 1);
	public static final ColorProfile POLLEN_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV, ACTUAL_POLLEN_COLOR,
			TOLERANT_POLLEN_COLOR);
	public static final ColorProfile RED_NECTAR_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV,
			ACTUAL_RED_NECTAR_COLOR, TOLERANT_RED_NECTAR_COLOR);
	public static final ColorProfile BLUE_NECTAR_COLOR_PROFILE = new ColorProfile(ColorSpace.HSV,
			ACTUAL_BLUE_NECTAR_COLOR, TOLERANT_BLUE_NECTAR_COLOR);
}
