package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Calc;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Launcher implements Mechanism {
	private final NextMotor leftLauncher = new NextMotor(Config.aModule, Config.aPort, Degrees.of(360.0 / 28.0));
	private final NextMotor rightLauncher = new NextMotor(Config.bModule, Config.bPort, Degrees.of(360.0 / 28.0));

	private double kV = 0.0;
	private double kS = 0.0;
	private double kP = 0.0;

	private final double READY_TOLERANCE = 25.0;
	private final double EXIT_TOLERANCE = 75.0;

	private final double IDLE_SPEED = 0.0;
	private final double CLOSE_SPEED = 0.0;
	private final double FAR_SPEED = 0.0;

	private double targetVelocity = 0.0;
	private double error = 0.0;

	private boolean powerMode = false;

	public void setPowerMode(boolean powerMode) {
		this.powerMode = powerMode;
	}

	public enum LauncherState {
		IDLE, SPINUP, READY
	}
	public LauncherState launcherState = LauncherState.IDLE;
	public boolean isIdle() {
		return launcherState == LauncherState.IDLE;
	}
	public void setIdle() {
		launcherState = LauncherState.IDLE;
	}
	public boolean isSpinUp() {
		return launcherState == LauncherState.SPINUP;
	}
	public void setSpinUp() {
		launcherState = LauncherState.SPINUP;
	}
	public boolean isReady() {
		return launcherState == LauncherState.READY;
	}
	public void setReady() {
		launcherState = LauncherState.READY;
	}
	public Launcher() {
		setIdle();

		leftLauncher.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);
		rightLauncher.setZeroPowerBehavior(NextMotor.ZeroPowerBehavior.FLOAT);

		leftLauncher.follow(rightLauncher, NextMotor.Direction.REVERSE);

		rightLauncher.getVelocityConstants().setKV(kV);
		rightLauncher.getVelocityConstants().setKS(kS);
		rightLauncher.getVelocityConstants().setKP(kP);
	}

	public double getPower() {
		return rightLauncher.getThrottle();
	}
	public double getTargetVelocity() {
		return targetVelocity;
	}
	public double getCurrentRPM() {
		return rightLauncher.getEncoderVelocity().into(RotationsPerMinute);
	}

	public void setKS(double kS) {
		this.kS = kS;
		rightLauncher.getVelocityConstants().setKS(kS);
	}
	public void increaseKS() {
		setKS(kS + 0.001);
	}
	public void decreaseKS() {
		setKS(Math.max(0, kS - 0.001));
	}

	public void setKP(double kP) {
		this.kP = kP;
		rightLauncher.getVelocityConstants().setKP(kP);
	}
	public void increaseKP() {
		setKP(kP + 0.0001);
	}
	public void decreaseKP() {
		setKP(Math.max(0, kP - 0.0001));
	}

	public boolean isAtVelocity() {
		return error <= READY_TOLERANCE;
	}

	public void updateState() {
		if (isIdle())
			return;

		error = Math.abs(getCurrentRPM() - targetVelocity);

		if (isReady()) {
			if (error > EXIT_TOLERANCE)
				setSpinUp();
		} else {
			if (error <= READY_TOLERANCE)
				setReady();
		}
	}

	public void setTargetVelocity(double RPM) {
		if (RPM == targetVelocity)
			return;
		targetVelocity = RPM;
		rightLauncher.setVelocitySetpoint(RotationsPerMinute.of(RPM));
		if (RPM == IDLE_SPEED) {
			setIdle();
			return;
		}
		setSpinUp();
	}
	public void incrementRPM() {
		setTargetVelocity(targetVelocity + 25);
	}

	public void decrementRPM() {
		setTargetVelocity(targetVelocity - 25);
	}

	public void increasePower() {
		rightLauncher.setThrottle(getPower() + 0.05);
	}

	public void decreasePower() {
		rightLauncher.setThrottle(getPower() - 0.05);
	}

	public void setClosePose() {
		setTargetVelocity(CLOSE_SPEED);
	}
	public void setFarPose() {
		setTargetVelocity(FAR_SPEED);
	}
	public void autoSetVelocity(Pose current) {
		setTargetVelocity(Calc.getVelocity(current));
	}
	public String PID() {
		return String.format("kV=%.6f  kS=%.4f  kP=%.5f", kV, kS, kP);
	}
	public void debug() {
		Telemetry.log("Launcher RPM" + getCurrentRPM());
		Telemetry.log("Launcher Power" + getPower());
	}

	@Override
	public void periodic() {
		updateState();

		if (isIdle() && !powerMode) {
			setTargetVelocity(IDLE_SPEED);
		}
	}

}
