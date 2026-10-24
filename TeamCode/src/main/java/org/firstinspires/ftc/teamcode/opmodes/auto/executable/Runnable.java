package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.Runners;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Test Auto")
public class Runnable extends NextOpMode {
    private final HazmatRobot robot;
    private final Runners runners;

    public Runnable(HazmatRobot robot) {
        super(robot);
        Alliance.currentAlliance = Alliance.BLUE;
        PathsAndPoses.mirror();
        this.robot = robot;
        runners = new Runners(robot);
    }

    @Override
    public void start() {
        robot.getFollower().setPose(PathsAndPoses.LeavePoses.startingPose);
        runners.preloadAuto().schedule();
    }

    @Override
    public void periodic() {
        robot.getFollower().update();
        telemetry.addData("POSE", robot.getFollower().pose());
    }
}
