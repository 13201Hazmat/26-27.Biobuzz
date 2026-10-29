package org.firstinspires.ftc.teamcode.opmodes.teleop;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextTeleop;

@NextTeleop(name = "Red TeleOp")
public class BlueTeleOp extends TeleOp{
    public BlueTeleOp() {
        super(new HazmatRobot(), Alliance.BLUE);
    }
}
