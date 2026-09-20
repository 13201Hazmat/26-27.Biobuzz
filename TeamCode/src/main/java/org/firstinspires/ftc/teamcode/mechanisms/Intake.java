package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.hardware.actuators.NextMotor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.units.measuretypes.AngularVelocity;

public class Intake implements Mechanism {
    private final double forward = 1.0;
    private final double reverse = -1.0;
    private final double off = 0.0;
    NextMotor intakeMotor = new NextMotor(RobotController.expansionHub(), Config.m);
    private IntakeState intakeState;
    private final double power;
    private AngularVelocity speed;

    public Intake() {
        intakeState = IntakeState.OFF;
        power = off;
        intakeMotor.setDirection(NextMotor.Direction.REVERSE);
    }

    private void setState(IntakeState intakeState) {
        this.intakeState = intakeState;
    }

    // USE THESE METHOD FOR INTAKE
    public Command setForward(){
        return instant(() -> this.setState(IntakeState.FORWARD));
    }

    public Command setReverse(){
        return instant(() -> this.setState(IntakeState.REVERSE));
    }
    public double getSpeed() {
        return intakeMotor.getThrottle();
    }

    public void cycle() {
        if (intakeState == IntakeState.FORWARD) intakeState = IntakeState.REVERSE;
        else if (intakeState == IntakeState.REVERSE) intakeState = IntakeState.OFF;
        else intakeState = IntakeState.FORWARD;
    }

    @Override
    public void periodic() {
        switch (intakeState) {
            case FORWARD:
                intakeMotor.setThrottle(forward);
                break;
            case REVERSE:
                intakeMotor.setThrottle(reverse);
                break;
            case OFF:
                intakeMotor.setThrottle(off);
                break;
        }
    }

    public enum IntakeState {
        FORWARD,
        REVERSE,
        OFF
    }
}