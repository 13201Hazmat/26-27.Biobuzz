package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;

public class Auto extends NextOpMode {
    private final HazmatRobot robot;
    public Auto(HazmatRobot robot) {
        super(robot);
        this.robot = robot;
    }
}
