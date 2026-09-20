package org.firstinspires.ftc.teamcode.opmodes.auto.runnable;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Auto")
public class Auto extends NextOpMode {

    private HazmatRobot robot;
    public Auto(HazmatRobot robot){
        super(robot);
        this.robot = robot;
    }
}
