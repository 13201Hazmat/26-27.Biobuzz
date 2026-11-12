package org.firstinspires.ftc.teamcode.mechanisms;

import static org.firstinspires.ftc.teamcode.data.Calc.clamp;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.data.Calc;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.telemetry.Telemetry;
public class Turret implements Mechanism {

	public static double targetTurretAng = 0.0;
	public static double KP = 0.0002;
	public static double TOLERANCE_DEG = 0.5;
	public static boolean AUTO_AIM = true;


	public static double CENTER_POS = 0.5;

	private final NextServo leftTurretServo = new NextServo(
			Config.leftTurretServoModule, Config.leftTurretServoPort, 0.01
	);
	private final NextServo rightTurretServo = new NextServo(
			Config.rightTurretServoModule, Config.rightTurretServoPort, 0.01
	);
	private final OctoQuad oq;

	private final double GEAR_RATIO = 0.9923076923;
	private final double SERVO_RANGE = 326;

	private final double TURRET_MAX_ANGLE = 175;
	private final double TURRET_MIN_ANGLE = -175;


	private final int ABS_CHANNEL = 4;
	private final int PWM_MIN = 1;
	private final int PWM_MAX = 1024;
	private final double ABS_ZERO_DEG = 0;

	private double curAngle = 0.0;
	private double servoPos = CENTER_POS;

	public Turret(HardwareMap hw) {
		OctoQuad tempOq = null;
		if (hw != null) {
			try {
				tempOq = hw.get(OctoQuad.class, "oq");
				tempOq.setChannelBankConfig(OctoQuad.ChannelBankConfig.BANK1_QUADRATURE_BANK2_PULSE_WIDTH);
				tempOq.setSingleChannelPulseWidthParams(
						ABS_CHANNEL,
						new OctoQuad.ChannelPulseWidthParams(PWM_MIN, PWM_MAX)
				);
			} catch (Exception e) {
				tempOq = null;
			}
		}
		this.oq = tempOq;
		targetTurretAng = curAngle;

		rightTurretServo.setPwmRange(550, 2450);
		leftTurretServo.setPwmRange(550, 2450);
	}

	public double getAngle() {
		return curAngle;
	}


	public double getRightTurretServoPos() {
		return rightTurretServo.getPosition();
	}

	public double getLeftTurretServoPos() {
		return leftTurretServo.getPosition();
	}


	public void toggleAutoAim() {
		AUTO_AIM = !AUTO_AIM;
	}

	private void setAutoAim(boolean on) {
		AUTO_AIM = on;
	}

	public void setTurretAngle(double angle) {
		targetTurretAng = normalizeAngle(angle);
	}

	public void setPositions(double p) {
		rightTurretServo.setPosition(p);
		leftTurretServo.setPosition(p);
	}


	public Command zeroTurretServo() {
		return instant(() -> setPositions(0));
	}

	public Command increment(double i) {
		return instant(() -> {
			rightTurretServo.setPosition(getRightTurretServoPos() + i);
			leftTurretServo.setPosition(getLeftTurretServoPos() + i);
		});
	}

	private void track(Pose g, Pose r) {
		targetTurretAng = normalizeAngle(Calc.turretKinematics(g, r));
	}

	public void trackGoal(Pose goal, Pose robot) {
		track(goal, robot);
	}

	public double normalizeAngle(double angDeg) {
		double a = ((angDeg % 360.0) + 360.0) % 360.0;
		return a > 180 ? a - 360 : a;
	}

	private double turretAngleToServo(double angle) {

		return CENTER_POS - angle * (GEAR_RATIO / SERVO_RANGE);
	}
	private int readPulse() {
		if (oq == null)
			return 0;
		try {
			return oq.readSinglePosition_Caching(ABS_CHANNEL);
		} catch (Exception e) {
			return 0;
		}
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

	private void update(boolean useOQ) {
		if (useOQ) {
			updateAngle();
			double error = targetTurretAng - curAngle;
			if (Math.abs(error) > TOLERANCE_DEG) {
				servoPos = clamp(servoPos - KP * error, turretAngleToServo(TURRET_MAX_ANGLE), turretAngleToServo(TURRET_MIN_ANGLE));
			}
			setPositions(servoPos);
		} else {
			servoPos = clamp(turretAngleToServo(targetTurretAng), turretAngleToServo(TURRET_MAX_ANGLE), turretAngleToServo(TURRET_MIN_ANGLE));
			setPositions(servoPos);
		}
	}

	public void debug() {

		Telemetry.log("Right Turret Servo Position", getRightTurretServoPos());
		Telemetry.log("Left Turret Servo Position", getLeftTurretServoPos());
		Telemetry.log("Target Angle", targetTurretAng);
		Telemetry.log("Current Angle", curAngle);
		Telemetry.log("Error", targetTurretAng - curAngle);
	}

	@Override
	public void periodic() {
		update(false);
	}
}