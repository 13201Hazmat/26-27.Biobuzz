package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.midPose_To_leavePose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.startingPose_To_midPose;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.HazPaths.startingPose_To_leavePose;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;

import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class Runners {

    private final HazmatRobot robot;

    public Runners(HazmatRobot robot) {
        this.robot = robot;
    }

    // MAKE SURE TO PUT PATHSANDPOSES.MIRROR()

    public Command leaveAuto() {
        return robot.moveToPoint(startingPose_To_leavePose);
    }

    public Command preloadAuto() {
        return sequential(
                robot.moveToPoint(startingPose_To_midPose),
                waitMs(1000),
                robot.moveToPoint(midPose_To_leavePose)
        );
    }

    public Command basicFlowerAuto() {
        return sequential(
        );
    }

}
