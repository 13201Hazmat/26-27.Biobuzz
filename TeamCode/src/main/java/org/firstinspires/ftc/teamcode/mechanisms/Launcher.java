package org.firstinspires.ftc.teamcode.mechanisms;

import static dev.nextftc.units.Units.Degrees;
import static dev.nextftc.units.Units.RotationsPerMinute;

import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Calc;
import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;

public class Launcher implements Mechanism {
    private final NextMotor leftLauncher = new NextMotor(Config.leftLauncherModule, Config.leftLauncherPort, Degrees.of(360.0/ 28.0) );
    private final NextMotor rightLauncher = new NextMotor(Config.rightLauncherModule, Config.rightLauncherPort, Degrees.of(360.0/ 28.0));

    private double kV = 0.0;
    private double kS = 0.0;
    private double kP = 0.0;
    private final double TOLERANCE = 25.0;

    private final double IDLE_SPEED = 0.0;
    private final double CLOSE_SPEED = 0.0;
    private final double FAR_SPEED = 0.0;

    private double targetVelocity = 0.0;

    private boolean powerMode = false;

    public void setPowerMode(boolean powerMode) {
        this.powerMode = powerMode;
    }

    public enum LauncherState {
        IDLE,
        SPINUP,
        READY
    }
    public LauncherState launcherState = LauncherState.IDLE;
    public boolean isIdle(){
        return launcherState == LauncherState.IDLE;
    }
    public void setIdle(){
        launcherState = LauncherState.IDLE;
    }
    public boolean isSpinUp(){
        return launcherState == LauncherState.SPINUP;
    }
    public void setSpinUp(){
        launcherState = LauncherState.SPINUP;
    }
    public boolean isReady(){
        return launcherState == LauncherState.READY;
    }
    public void setReady(){
        launcherState = LauncherState.READY;
    }
    public Launcher(){
        setIdle();

        leftLauncher.follow(rightLauncher, NextMotor.Direction.REVERSE);

        rightLauncher.getVelocityConstants().setKV(kV);
        rightLauncher.getVelocityConstants().setKS(kS);
        rightLauncher.getVelocityConstants().setKP(kP);
    }
    public double getPower(){
        return rightLauncher.getThrottle();
    }
    public double getCurrentRPM(){
        return rightLauncher.getEncoderVelocity().into(RotationsPerMinute);
    }
    public boolean isAtVelocity(){
        return Math.abs(getCurrentRPM() - targetVelocity) <= TOLERANCE;
    }
    public void updateState() {
        if (!isIdle()) {
            if (isAtVelocity()) {
                setReady();
            } else {
                setSpinUp();
            }
        }
    }

    public void setTargetVelocity(double RPM) {
        targetVelocity = RPM;
        rightLauncher.setVelocitySetpoint(RotationsPerMinute.of(RPM));
        if (RPM == IDLE_SPEED) { setIdle(); return; }
        setSpinUp();
    }
    public void incrementRPM(){
        setTargetVelocity(targetVelocity + 25);
    }

    public void decrementRPM(){
        setTargetVelocity(targetVelocity - 25);
    }

    public void increasePower(){
        rightLauncher.setThrottle(getPower() + 0.05);
    }

    public void decreasePower(){
        rightLauncher.setThrottle(getPower() - 0.05);
    }

    public void setClosePose(){
        setTargetVelocity(CLOSE_SPEED);
    }
    public void setFarPose(){
        setTargetVelocity(FAR_SPEED);
    }
    public void autoSetVelocity(Pose current){
        setTargetVelocity(Calc.getVelocity(current));
    }
    public String debug(){
        return "Launcher RPM:" + getCurrentRPM() + ", Launcher Power:" + getPower();
    }

    @Override
    public void periodic(){
        updateState();

        if(isIdle() && !powerMode){
            setTargetVelocity(IDLE_SPEED);
        }
    }


}
