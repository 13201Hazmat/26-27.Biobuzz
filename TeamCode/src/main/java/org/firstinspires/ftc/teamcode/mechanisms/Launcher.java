package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Calc;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Launcher implements Mechanism {
	private final NextMotor rightLauncher = new NextMotor(Config.bModule, Config.bPort, Degrees.of(360.0 / 28.0));
	private final NextMotor leftLauncher = new NextMotor(Config.aModule, Config.aPort, Degrees.of(360.0 / 28.0));
	private final NextServo launcherGate = new NextServo(Config.mModule, Config.mPort);
	private double kV = 0.0;
	private double kS = 0.0;
	private double kP = 0.0;

	private final double READY_TOLERANCE = 25.0;
	private final double EXIT_TOLERANCE = 75.0;

	private final double IDLE_SPEED = 0.0;
	private final double CLOSE_SPEED = 0.0;
	private final double FAR_SPEED = 0.0;

	private final double OPEN_POS = 1.0;
	private final double CLOSE_POS = 0.0;

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
	public void test() {
		setPowerMode(true);
		leftLauncher.follow(rightLauncher, NextMotor.Direction.REVERSE);
		rightLauncher.setThrottle(1.0);
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
		setPowerMode(false);
		if (RPM == targetVelocity)
			return;
		targetVelocity = RPM;
		leftLauncher.follow(rightLauncher, NextMotor.Direction.REVERSE);
		rightLauncher.setVelocitySetpoint(RotationsPerMinute.of(RPM));
		if (RPM == IDLE_SPEED) {
			setIdle();
			return;
		}
		setSpinUp();
	}

	private Command setGatePosition(double pos) {
		return instant(() -> launcherGate.setPosition(pos));
	}
	public Command openGate() {
		return setGatePosition(OPEN_POS);
	}
	public Command closeGate() {
		return setGatePosition(CLOSE_POS);
	}

	public void incrementRPM() {
		setTargetVelocity(targetVelocity + 25);
	}

	public void decrementRPM() {
		setTargetVelocity(targetVelocity - 25);
	}

	public void increasePower() {
		setPowerMode(true);
		leftLauncher.follow(rightLauncher, NextMotor.Direction.REVERSE);
		rightLauncher.setThrottle(Math.min(1.0, getPower() + 0.1));
	}

	public void decreasePower() {
		setPowerMode(true);
		leftLauncher.follow(rightLauncher, NextMotor.Direction.REVERSE);
		rightLauncher.setThrottle(Math.max(-1.0, getPower() - 0.1));
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
	public void PID() {
		Telemetry.log("kV", kV);
		Telemetry.log("kS", kS);
		Telemetry.log("kP", kP);
	}
	public void debug() {
		Telemetry.log("Launcher RPM", getCurrentRPM());
		Telemetry.log("Right Power", rightLauncher.getThrottle());
		Telemetry.log("Left Power", leftLauncher.getThrottle());
	}

	@Override
	public void periodic() {
		updateState();

		if (isIdle() && !powerMode) {
			setTargetVelocity(IDLE_SPEED);
		}
	}

}