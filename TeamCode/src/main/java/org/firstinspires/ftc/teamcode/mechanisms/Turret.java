package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.data.Calc;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.control.feedback.PIDCoefficients;
import dev.nextftc.control.feedback.PIDController;
import dev.nextftc.control.feedforward.SimpleFeedforward;
import dev.nextftc.hardware.actuators.NextCRServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Turret implements Mechanism {
    private final NextCRServo leftTurretServo = new NextCRServo(Config.eModule, Config.ePort);
    private final NextCRServo rightTurretServo = new NextCRServo(Config.eModule, Config.ePort);
    private final OctoQuad oq;

    public static double kP = 0.0;
    public static double kD = 0.0;
    public static double kS = 0.0;
    public static double kV = 0.0;
    private double lastKs = kS;
    private double lastKv = kV;
    private double lastKp = kP;
    private double lastKd = kD;

    private final double CLOSE_ENOUGH_DEG = 0.75;

    private final double MIN_DEG = 0;
    private final double MAX_DEG = 300;

    private final int ABS_CHANNEL = 4;
    private final int PWM_MIN = 4;
    private final int PWM_MAX = 998;
    private final double ABS_ZERO_DEG = 0;

    private PIDController pid = new PIDController(new PIDCoefficients(kP, 0, kD));
    private SimpleFeedforward ff = new SimpleFeedforward(kS, kV, 0);

    private double targetDeg = 0.0;
    private double curAngle = 0.0;
    private double feedforwardVel = 0;

    private boolean enabled = true;

    public Turret(HardwareMap hw) {
        oq = hw.get(OctoQuad.class, "oq");
        oq.setChannelBankConfig(OctoQuad.ChannelBankConfig.BANK1_QUADRATURE_BANK2_PULSE_WIDTH);
        oq.setSingleChannelPulseWidthParams(ABS_CHANNEL,
                new OctoQuad.ChannelPulseWidthParams(PWM_MIN, PWM_MAX));
        int pulse = readPulse();
        if (pulse > 0) {
            curAngle = pulseToDeg(pulse);
        }
        targetDeg = curAngle;
    }

    /**
     * Enables or disables the control loop without stopping the servos directly.
     * While disabled, {@link #update()} outputs zero power.
     *
     * @param on true to enable the control loop, false to disable it
     */
    public void autoAim(boolean on) {
        enabled = on;
    }

    /** Disables the control loop and immediately sets servo power to zero. */
    public void stop() {
        enabled = false;
        setPower(0);
    }

    /**
     * Reads the absolute encoder pulse length using the OctoQuad cached read.
     *
     * @return pulse width in microseconds (0 or fewer means no signal)
     */
    private int readPulse() {
        return oq.readSinglePosition_Caching(ABS_CHANNEL);
    }

    /**
     * Wraps an angle into [{@code MIN_DEG}, {@code MIN_DEG} + 360 so the encoder seam falls
     * outside the turret's travel range.
     *
     * @param deg angle in degrees, any value
     * @return equivalent angle in degrees inside the wrapped range
     */
    private double wrap(double deg) {
        return ((deg - MIN_DEG) % 360.0 + 360.0) % 360.0 + MIN_DEG;
    }

    /**
     * Converts an absolute encoder pulse length to a turret angle.
     *
     * @param pulse pulse width in microseconds
     * @return turret angle in degrees, within the turret's travel range
     */
    private double pulseToDeg(int pulse) {
        double raw = (pulse - PWM_MIN) / (double) (PWM_MAX - PWM_MIN) * 360.0;
        return wrap(raw - ABS_ZERO_DEG);
    }

    /**
     * Gets the current turret angle from the absolute encoder.
     *
     * @return turret angle in degrees
     */
    public double getAngle() {
        return pulseToDeg(readPulse());
    }

    /**
     * Gets the current target angle.
     *
     * @return target angle in degrees
     */
    public double getTargetDeg() {
        return targetDeg;
    }

    /**
     * Gets the angle error using the angle cached by the last {@link #update()}.
     *
     * @return target minus current angle, in degrees
     */
    public double getError() {
        return targetDeg - curAngle;
    }

    /**
     * Checks whether the turret is within tolerance of its target.
     *
     * @return true if the absolute error is under {@code CLOSE_ENOUGH_DEG}
     */
    public boolean atTarget() {
        return Math.abs(getError()) < CLOSE_ENOUGH_DEG;
    }

    /**
     * Applies the same power to both turret servos.
     *
     * @param p servo power, typically in the range [-1, 1]
     */
    public void setPower(double p) {
        leftTurretServo.setPower(p);
        rightTurretServo.setPower(p);
    }

    /**
     * Sets the target angle directly, clamped to the turret limits.
     *
     * @param deg desired turret angle in degrees
     */
    public void setTargetDeg(double deg) {
        targetDeg = Math.max(MIN_DEG, Math.min(MAX_DEG, deg));
    }

    /**
     * Sets the target from an aim angle computed by the field kinematics, converting it to a
     * turret angle.
     * <p>
     * Because the travel range is under 360 degrees, only one equivalent angle can fit inside
     * it. If the aim lands in the dead zone between {@code MAX_DEG} and the seam, the target
     * goes to the nearer limit.
     *
     * @param fieldDeg desired aim angle in degrees
     */
    private void setTargetFieldDeg(double fieldDeg) {
        double d = wrap(180 - fieldDeg);
        if (d > MAX_DEG) {
            double distToMax = d - MAX_DEG;
            double distToMin = (MIN_DEG + 360.0) - d;
            d = distToMax < distToMin ? MAX_DEG : MIN_DEG;
        }
        targetDeg = d;
    }

    /**
     * Computes and sets the turret target needed to point at a goal.
     * <p>
     * The aim angle comes from {@link Calc#turretKinematics(Pose, Pose)} (radians), which is
     * converted to degrees and passed to {@link #setTargetFieldDeg(double)}.
     *
     * @param g goal pose, in inches
     * @param r robot pose, with heading in radians
     */
    private void track(Pose g, Pose r) {
        setTargetFieldDeg(Math.toDegrees(Calc.turretKinematics(g, r)));
    }

    /**
     * Aims the turret at a goal without rotation feedforward.
     *
     * @param goal goal pose, in inches
     * @param robot current robot pose, with heading in radians
     */
    public void trackGoal(Pose goal, Pose robot) {
        track(goal, robot);
        feedforwardVel = 0;
    }

    /**
     * Aims the turret at a goal and feeds forward the robot's angular velocity so the turret
     * can counter-rotate while the robot turns.
     *
     * @param goal goal pose, in inches
     * @param robot current robot pose, with heading in radians
     * @param omega robot angular velocity in radians per second
     */
    public void trackGoal(Pose goal, Pose robot, double omega) {
        track(goal, robot);
        feedforwardVel = Math.toDegrees(omega);
    }

    /**
     * Runs one iteration of the control loop.
     * <p>
     * Refreshes the cached angle, rebuilds the PD and feedforward controllers if their gains
     * changed, then outputs {@code PD(error) + feedforward(velocity)} to the servos. If the
     * encoder has no signal or the turret is disabled, power is set to zero.
     */
    private void update() {
        int pulse = readPulse();

        if (pulse < 1) {
            setPower(0);
            return;
        }
        curAngle = pulseToDeg(pulse);

        if (!enabled) {
            setPower(0);
            return;
        }

        if (kP != lastKp || kD != lastKd) {
            pid = new PIDController(new PIDCoefficients(kP, 0, kD));
            lastKp = kP;
            lastKd = kD;
        }
        if (kS != lastKs || kV != lastKv) {
            ff = new SimpleFeedforward(kS, kV, 0.0);
            lastKs = kS;
            lastKv = kV;
        }

        setPower(pid.calculate(targetDeg - curAngle) + ff.calculate(feedforwardVel));
    }

    /** Logs servo power, current angle, target, and error to telemetry for tuning. */
    public void debug() {
        Telemetry.log("Servo Power", rightTurretServo.getPower());
        Telemetry.log("Turret Angle", getAngle());
        Telemetry.log("Raw Pulse MicroSec", readPulse());
        Telemetry.log("Target", targetDeg);
        Telemetry.log("Error", getError());
    }

    /** Called by the NextFTC scheduler each loop. Runs {@link #update()}. */
    @Override
    public void periodic() {
        update();
    }
}