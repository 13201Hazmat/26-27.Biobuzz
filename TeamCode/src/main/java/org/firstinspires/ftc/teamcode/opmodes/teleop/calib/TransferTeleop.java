package org.firstinspires.ftc.teamcode.opmodes.teleop.calib;

import static com.pedropathing.ivy.commands.Commands.instant;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextOpMode;
import dev.nextftc.robot.opmode.NextTeleop;
import dev.nextftc.robot.triggers.CommandGamepad;
import dev.nextftc.robot.triggers.Trigger;

@NextTeleop(name = "Transfer Test")
public class TransferTeleop extends NextOpMode {
    private final HazmatRobot robot;
    public TransferTeleop(HazmatRobot robot) {
        super(robot);
        this.robot = robot;
        robot.getTransfer().init();
        Trigger.Companion.getDefaultEventLoop().clear();
        CommandGamepad gp1 = new CommandGamepad(gamepad1);

        gp1.leftBumper().onTrue(instant(() -> robot.getIntake().cycle()));
        gp1.rightBumper().onTrue(robot.getTransfer().setForward());
        gp1.triangle().onTrue(robot.getTransfer().setReverse());
        gp1.circle().onTrue(robot.getTransfer().setFull());
        gp1.square().onTrue(instant(() -> robot.getTransfer().getTransferSensing().removeTopBall()));
    }

    @Override
    public void periodic() {
        robot.printTelemetry();
        telemetry.update();
    }
}
