package org.firstinspires.ftc.teamcode.opmodes.teleop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Main")
public abstract class BaseTeleop extends NextOpMode {
    protected final HazmatRobot robot;
    protected CommandGamepad gp1 = new CommandGamepad(gamepad1);

    public BaseTeleop(HazmatRobot robot) {
        super(robot);
        this.robot = robot;

        Trigger.Companion.getDefaultEventLoop().clear();
    }

    @Override
    public void start() {

    }

    @Override
    public void periodic() {

    }
}
