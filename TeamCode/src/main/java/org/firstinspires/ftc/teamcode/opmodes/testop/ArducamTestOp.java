package org.firstinspires.ftc.teamcode.opmodes.testop;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.telemetry.Telemetry;
import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Arducam Test")
public class ArducamTestOp extends NextOpMode {
    private final HazmatRobot robot;

    public ArducamTestOp(HazmatRobot robot) {
        super(robot);
        this.robot = robot;

        robot.setActiveOpMode(this);

        Trigger.Companion.getDefaultEventLoop().clear();
    }

    @Override
    public void start() {
        CommandGamepad gp1 = new CommandGamepad(gamepad1);

        robot.getDrivetrain().startDrive(gamepad1);

    }

    @Override
    public void periodic() {
        robot.getArducam().debug();
    }

}
