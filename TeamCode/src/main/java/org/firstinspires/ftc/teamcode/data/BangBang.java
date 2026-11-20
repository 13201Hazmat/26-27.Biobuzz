package org.firstinspires.ftc.teamcode.data;


public class BangBang {

    private final double power;
    private final double hysteresis;

    public BangBang(double power, double hysteresis) {
        this.power = power;
        this.hysteresis = hysteresis;
    }


    public double calculate(double error) {
        return (error > hysteresis) ? power : 0.0;
    }
    public double calculate(double goalVelocity, double measuredVelocity) {
        return calculate(goalVelocity - measuredVelocity);
    }

    public void reset() {
        // nothing to reset
    }
}