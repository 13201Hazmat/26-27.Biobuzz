package org.firstinspires.ftc.teamcode.mechanisms;

import static org.firstinspires.ftc.teamcode.data.Calc.clamp;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.data.Calc;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Turret implements Mechanism {
	private final NextServo leftTurretServo = new NextServo(Config.eModule, Config.ePort);
	private final NextServo rightTurretServo = new NextServo(Config.eModule, Config.ePort);
	private final OctoQuad oq;

	public static double targetTurretAng = 0.0;
	public static double KP = 0.0002;
	public static double TOLERANCE_DEG = 0.5;
	public static boolean AUTO_AIM = true;

	private double curAngle = 0.0;
	private double servoPos = 0.5;

	private final double GEAR_RATIO = 0.9923076923;
	private final double SERVO_RANGE = 326;

	private final int ABS_CHANNEL = 4;
	private final int PWM_MIN = 1;
	private final int PWM_MAX = 1024;
	private final double ABS_ZERO_DEG = 0;

	public Turret(HardwareMap hw) {
		oq = hw.get(OctoQuad.class, "oq");
		oq.setChannelBankConfig(OctoQuad.ChannelBankConfig.BANK1_QUADRATURE_BANK2_PULSE_WIDTH);
		oq.setSingleChannelPulseWidthParams(
			ABS_CHANNEL,
			new OctoQuad.ChannelPulseWidthParams(PWM_MIN, PWM_MAX)
		);
		updateAngle();
		targetTurretAng = curAngle;
		servoPos = clamp(turretAngleToServo(curAngle), 0, 1);
	}

	private void setAutoAim(boolean on) {
		AUTO_AIM = on;
	}

	public void toggleAutoAim() {
		AUTO_AIM = !AUTO_AIM;
	}

	private int readPulse() {
		return oq.readSinglePosition_Caching(ABS_CHANNEL);
	}

	private double pulseToDeg(int pulse) {
		double frac = (pulse - PWM_MIN) / (double) (PWM_MAX - PWM_MIN);
		return normalizeAngle(frac * 360.0 + ABS_ZERO_DEG);
	}

	private void updateAngle() {
		int pulse = readPulse();
		if (pulse > 0)
			curAngle = pulseToDeg(pulse);
	}

	public double getAngle() {
		return curAngle;
	}

	public double normalizeAngle(double angDeg) {
		return ((angDeg % 360.0) + 360.0) % 360.0;
	}

	private double turretAngleToServo(double angle) {
		return angle * (GEAR_RATIO / SERVO_RANGE);
	}

	public void setTurretAngle(double angle) {
		targetTurretAng = normalizeAngle(angle);
	}

	private void track(Pose g, Pose r) {
		targetTurretAng = normalizeAngle(Calc.turretKinematics(g, r));
	}

	public void trackGoal(Pose goal, Pose robot) {
		track(goal, robot);
	}

	public void setPositions(double p) {
		rightTurretServo.setPosition(p);
		leftTurretServo.setPosition(p);
	}

	private void update() {
		double error = targetTurretAng - curAngle;
		if (Math.abs(error) > TOLERANCE_DEG) {
			servoPos = clamp(servoPos + KP * error, 0, 1);
		}
		setPositions(servoPos);
	}

	public void debug() {
		Telemetry.log("Servo Position", rightTurretServo.getPosition());
		Telemetry.log("Target Angle", targetTurretAng);
		Telemetry.log("Current Angle", curAngle);
		Telemetry.log("Error", targetTurretAng - curAngle);
	}

	@Override
	public void periodic() {
		updateAngle();
		if (AUTO_AIM) {
			update();
		}
	}
}