package org.firstinspires.ftc.teamcode.vision;

public final class HiveConfig {
	private HiveConfig() {
	}

	public static String LIMELIGHT_NAME = "Limelight";
	public static int LIMELIGHT_PIPELINE = 0;
	public static int LIMELIGHT_POLL_RATE_HZ = 100;

	public static double CAM_PITCH_DEG = 25.0; // camera tilt BELOW horizontal
	public static double CAM_H = 9.2; // camera lens height above floor, in
	public static double BALL_H = 3.0; // ball center height above floor, in
	public static double CAM_X_OFFSET = 0.0; // camera FORWARD of robot center, in
	public static double CAM_Y_OFFSET = 0.0; // camera LEFT of robot center, in
	public static double HFOV_DEG = 54.5;
	public static double VFOV_DEG = 42.0;
	public static double FOV_MARGIN_DEG = 4.0;

	public static int CLASS_YELLOW_NEUTRAL = 0;
	public static int CLASS_RED = 1;
	public static int CLASS_BLUE = 2;

	public static double MIN_CONF = 0.44;
	public static long MAX_STALENESS_MS = 150;
	public static double LOCK_GATE_DEG = 12.0;
	public static long LOCK_LOST_MS = 300;
	public static int CONFIRM_FRAMES = 0;
	public static double CONFIRM_GATE_DEG = 12.0;

	public static double STOP_DIST = 16.0;
	public static double AIM_TOL_DEG = 5.0;
	public static double DRIVE_MIN_TX = 15.0;
	public static double DRIVE_KP = 0.03;
	public static double MIN_FWD = 0.15;
	public static double MAX_FWD = 0.7;
	public static double TURN_KP = 0.025;
	public static double MIN_TURN = 0.08;
	public static double MAX_TURN = 0.6;
	public static double SEARCH_TURN = 0.30;
	public static double SEARCH_SWEEP_DEG = 360.0;
	public static double TURN_RATE_RAD_PER_POWER_SEC = 3.5;
	public static double COAST_MAX_DIST = 26.0;
	public static double COAST_POWER = 0.25;
	public static long COAST_MS = 450;
	public static long CHASE_LOST_MS = 500;
	public static long PICKUP_DWELL_MS = 600;
	public static long PICKUP_CONFIRM_MS = 800;
	public static double INTAKE_POWER = 1.0;
	public static long PREDICT_TURN_MS = 150;

	public static double MAX_PLAN_RANGE = 40.0;
	public static double MERGE_RADIUS = 4.0;
	public static double FOLLOWER_APPROACH_DIST = 24.0;
	public static double CLEAR_RADIUS = 12.0;
	public static double FIELD_MIN = 6.0;
	public static double FIELD_MAX = 138.0;
	public static long TRAVEL_TIMEOUT_MS = 4000;
	public static double HUNT_BUDGET_SEC = 20.0;
	public static long SETTLE_MS = 250;
	public static long SCAN_MS = 250;
	public static double SEARCH_STEP_DEG = 45.0;
	public static int SEARCH_MAX_STEPS = 8;
	public static double TURN_TO_KP = 0.8;
	public static double TURN_TO_MIN = 0.05;
	public static double TURN_TO_MAX = 0.5;
	public static double TURN_TO_TOL_DEG = 4.0;
	public static long TURN_TIMEOUT_MS = 2500;
}
