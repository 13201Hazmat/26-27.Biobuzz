package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.ivy.Command;

import org.firstinspires.ftc.teamcode.data.Config;

import dev.nextftc.hardware.actuators.NextServo;
import dev.nextftc.robot.Mechanism;

public class LauncherHood implements Mechanism {
    private final NextServo launcherHood = new NextServo(Config.launcherHoodModule, Config.launcherHoodPort, 0.0);

    private final double POLLEN_POS = 0.0;
    private final double NECTAR_POS = 0.0;

    public LauncherHood (){
        launcherHood.setPosition(POLLEN_POS);
    }

    public double getServoPos(){
        return launcherHood.getPosition();
    }

    public Command setServoPos(double pos){
        return instant(()->launcherHood.setPosition(pos));
    }

    public Command incrementUp(){
        return setServoPos(getServoPos() + 0.01);
    }

    public Command incrementDown(){
        return setServoPos(getServoPos() - 0.01);
    }
    public Command setPollenPos(){
        return setServoPos(POLLEN_POS);
    }

    public Command setNectarPos(){
        return setServoPos(NECTAR_POS);
    }

    public String debug(){
        return "Hood Servo Position:" + getServoPos();
    }
}
