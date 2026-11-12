package org.firstinspires.ftc.teamcode.opmodes.teleop;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextTeleop;

@NextTeleop(name = "Red TeleOp")
public class RedTeleOp extends Tele {
	public RedTeleOp() {
		super(new HazmatRobot(), Alliance.RED);
	}
}
