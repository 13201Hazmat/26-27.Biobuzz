package org.firstinspires.ftc.teamcode.opmodes.teleop;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "hi")
public class Teleop extends NextOpMode {
    private final HazmatRobot robot;
    public Teleop(HazmatRobot robot){
        super(robot);
        this.robot = robot;

        Trigger.Companion.getDefaultEventLoop().clear();
        CommandGamepad gp1 = new CommandGamepad(gamepad1);

        gp1.rightBumper().onTrue(robot.getIntake().setForward());
    }

    @Override
    public void periodic() {

    }
}
