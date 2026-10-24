package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;
import dev.nextftc.robot.opmode.NextOpMode;

@NextAutonomous(name = "Test Auto")
public class HazmatAuto extends NextOpMode {
    protected final HazmatRobot robot;
    protected final Runners runners;
    protected final Pose startingPose;
    protected Command autoCommand;

    protected HazmatAuto(HazmatRobot robot, Alliance alliance, Pose startingPose) {
        super(robot);
        this.robot = robot;
        this.startingPose = startingPose;
        runners = new Runners(robot);

        Alliance.currentAlliance = alliance;
        PathsAndPoses.mirror();
    }

    public void setAutoCommand(Command command) {
        this.autoCommand = command;
    }

    @Override
    public void start() {
        robot.getFollower().setPose(startingPose);
        autoCommand.schedule();
    }

    @Override
    public void periodic() {
        robot.getFollower().update();
        telemetry.addData("POSE", robot.getFollower().pose());
    }
}
