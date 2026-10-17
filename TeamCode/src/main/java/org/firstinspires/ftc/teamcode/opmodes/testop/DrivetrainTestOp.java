package org.firstinspires.ftc.teamcode.opmodes.testop;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.opmode.NextUtility;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextUtility(name = "Drivetrain Test Teleop")
public class DrivetrainTestOp extends NextOpMode {
    private final HazmatRobot robot;
    public DrivetrainTestOp(HazmatRobot robot) {
        super(robot);
        this.robot = robot;

        Trigger.Companion.getDefaultEventLoop().clear();
        CommandGamepad gp1 = new CommandGamepad(gamepad1);
    }

    @Override
    public void start() {
        robot.getDrivetrain().startDrive(gamepad1, 90);
    }

    @Override
    public void periodic() {
//        robot.getIntake().debug();
    }
}
