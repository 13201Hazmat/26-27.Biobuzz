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
import dev.nextftc.robot.Telemetry;

public class Turret implements Mechanism {

	public static double targetTurretAng = 0.0;
	public static double KP = 0.0002;
	public static double TOLERANCE_DEG = 0.5;
	public static boolean AUTO_AIM = true;

	private final NextServo leftTurretServo = new NextServo(
		Config.leftTurretServoModule, Config.leftTurretServoPort, 0.0
	);
	private final NextServo rightTurretServo = new NextServo(
		Config.rightTurretServoModule, Config.rightTurretServoPort, 0.0
	);
	private final OctoQuad oq;

	private final double GEAR_RATIO = 0.9923076923;
	private final double SERVO_RANGE = 326;

	private final int ABS_CHANNEL = 4;
	private final int PWM_MIN = 1;
	private final int PWM_MAX = 1024;
	private final double ABS_ZERO_DEG = 0;

	private double curAngle = 0.0;
	private double servoPos = 0.5;

	public Turret(HardwareMap hw) {
		OctoQuad tempOq = null;
		try {
			if (hw != null) {
				tempOq = hw.get(OctoQuad.class, "oq");
				if (tempOq != null) {
					tempOq.setChannelBankConfig(OctoQuad.ChannelBankConfig.BANK1_QUADRATURE_BANK2_PULSE_WIDTH);
					tempOq.setSingleChannelPulseWidthParams(
						ABS_CHANNEL,
						new OctoQuad.ChannelPulseWidthParams(PWM_MIN, PWM_MAX)
					);
				}
			}
		} catch (Throwable t) {
			tempOq = null;
		}
		this.oq = tempOq;
		updateAngle();
		targetTurretAng = curAngle;
		servoPos = clamp(turretAngleToServo(curAngle), 0, 1);
	}

	public double getAngle() {
		return curAngle;
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

	public Command incrementTurret() {
		return instant(() -> {
			rightTurretServo.setPosition(rightTurretServo.getPosition() + 0.01);
			leftTurretServo.setPosition(leftTurretServo.getPosition() + 0.01);
		});
	}

	public Command decrementTurret() {
		return instant(() -> {
			rightTurretServo.setPosition(rightTurretServo.getPosition() - 0.01);
			leftTurretServo.setPosition(leftTurretServo.getPosition() - 0.01);
		});
	}

	public void trackGoal(Pose goal, Pose robot) {
		track(goal, robot);
	}

	private void track(Pose g, Pose r) {
		targetTurretAng = normalizeAngle(Calc.turretKinematics(g, r));
	}

	public double normalizeAngle(double angDeg) {
		return ((angDeg % 360.0) + 360.0) % 360.0;
	}

	private double turretAngleToServo(double angle) {
		return angle * (GEAR_RATIO / SERVO_RANGE);
	}

	private double pulseToDeg(int pulse) {
		double frac = (pulse - PWM_MIN) / (double) (PWM_MAX - PWM_MIN);
		return normalizeAngle(frac * 360.0 + ABS_ZERO_DEG);
	}

	private int readPulse() {
		if (oq == null)
			return 0;
		try {
			return oq.readSinglePosition_Caching(ABS_CHANNEL);
		} catch (Throwable t) {
			return 0;
		}
	}

	private void updateAngle() {
		int pulse = readPulse();
		if (pulse > 0)
			curAngle = pulseToDeg(pulse);
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