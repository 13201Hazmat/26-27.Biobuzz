package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runners;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Example Autonomous")
public class ExampleAuto extends NextOpMode {
	private final HazmatRobot robot;
	private final Runners runners;

	public ExampleAuto(HazmatRobot robot) {
		super(robot);
		this.robot = robot;
		runners = new Runners(this.robot);
	}

	@Override
	public void start() {
		runners.leaveAuto().schedule();
	}
}
