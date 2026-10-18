package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Turret implements Mechanism {
    private final NextCRServo leftTurretServo = new NextCRServo(Config.eModule, Config.ePort);
    private final NextCRServo rightTurretServo = new NextCRServo(Config.eModule, Config.ePort);
    private final OctoQuad oq;

    public static double kP = 0.0;
    public static double kD = 0.0;
    private double lastKp = kP;
    private double lastKd = kD;

    private final double CLOSE_ENOUGH_DEG = 0.75;

    private final int OQ_CHANNEL = 2;
    private final int TICKS_PER_REV = 8192;
    private final double GEAR_RATIO = 0;
    private final double MIN_DEG = 0;
    private final double MAX_DEG = 0;

    private PIDController pid = new PIDController(new PIDCoefficients(kP, 0, kD));

    private int encoderOffsetTicks = 0;
    private double targetDeg = 0.0;
    private double curAngle = 0.0;

    private boolean enabled = true;

    public Turret(HardwareMap hw) {
        oq = hw.get(OctoQuad.class, "oq");
    }

    public void zero(int startTicks) {
        oq.resetSinglePosition(OQ_CHANNEL);
        encoderOffsetTicks = startTicks;
        curAngle = getAngle();
        targetDeg = curAngle;
    }

    public void autoAim(boolean on) {
        enabled = on;
    }
    public void stop() {
        enabled = false;
        setPower(0);
    }

    public int getTicks() {
        return oq.readSinglePosition_Caching(OQ_CHANNEL) + encoderOffsetTicks;
    }
    public double getAngle() {
        return ticksToDegrees(getTicks());
    }
    public double getTargetDeg() {
        return targetDeg;
    }
    public double getError() {
        return targetDeg - curAngle;
    }
    public boolean atTarget() {
        return Math.abs(getError()) < CLOSE_ENOUGH_DEG;
    }
    public double ticksToDegrees(double ticks) {
        return ticks / TICKS_PER_REV / GEAR_RATIO * 360.0;
    }

    public void setPower(double p) {
        leftTurretServo.setPower(p);
        rightTurretServo.setPower(p);
    }
    public void setTargetDeg(double deg) {
        targetDeg = Math.max(MIN_DEG, Math.min(MAX_DEG, deg));
    }

    private double closestTargetAngle(double target) {
        target %= 360.0;
        if (target < 0)
            target += 360.0;
        double delta = target - (curAngle % 360.0);
        delta = (delta + 540.0) % 360.0 - 180.0;
        return curAngle + delta;
    }
    private void setTargetFieldDeg(double fieldDeg) {
        double robotDeg = closestTargetAngle(180 - fieldDeg);
        while (robotDeg > MAX_DEG)
            robotDeg -= 360;
        while (robotDeg < MIN_DEG)
            robotDeg += 360;
        targetDeg = robotDeg;
    }
    public void aimAt(Pose goal, Pose robot) {
        double fieldAngle = Math.atan2(goal.y() - robot.y(), goal.x() - robot.x());
        setTargetFieldDeg(Math.toDegrees(fieldAngle - robot.heading()));
    }

    public void update() {
        curAngle = getAngle();

        if (!enabled) {
            setPower(0);
            return;
        }

        if (kP != lastKp || kD != lastKd) {
            pid = new PIDController(new PIDCoefficients(kP, 0, kD));
            lastKp = kP;
            lastKd = kD;
        }
        setPower(pid.calculate(targetDeg - curAngle));
    }

    public void debug() {
        Telemetry.log("Servo Power", rightTurretServo.getPower());
        Telemetry.log("Turret Angle", getAngle());
        Telemetry.log("Target", targetDeg);
        Telemetry.log("Error", getError());
    }

    @Override
    public void periodic() {
        update();
    }
}