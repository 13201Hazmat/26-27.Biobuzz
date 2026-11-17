package org.firstinspires.ftc.teamcode.mechanisms;

import static org.firstinspires.ftc.teamcode.data.Calc.clamp;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

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

	private final NextServo leftTurretServo = new NextServo(
		Config.leftTurretServoModule, Config.leftTurretServoPort, 0.01
	);
	private final NextServo rightTurretServo = new NextServo(
		Config.rightTurretServoModule, Config.rightTurretServoPort, 0.01
	);

	private final double GEAR_RATIO = 0.9923076923;
	private final double SERVO_RANGE = 326;

	private double curAngle = 0.0;
	private double servoPos = 0.0;

	public Turret() {
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
		return ((angDeg % 360.0) + 360.0) % 360.0;
	}

	private double turretAngleToServo(double angle) {
		return angle * (GEAR_RATIO / SERVO_RANGE);
	}

	private void update() {
		servoPos = clamp(
			turretAngleToServo(targetTurretAng), 0.0, 1.0
		);
		setPositions(servoPos);
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
		update();
	}
}