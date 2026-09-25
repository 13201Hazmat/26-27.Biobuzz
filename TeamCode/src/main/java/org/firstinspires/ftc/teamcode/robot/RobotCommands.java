package org.firstinspires.ftc.teamcode.robot;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.commands.Commands.instant;

import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

/**
 * Class to store robot commands that utilize more than 1 subsystem.
 */
public class RobotCommands {
    private final HazmatRobot robot;
    public RobotCommands(HazmatRobot robot){
        this.robot = robot;
    }

    public Command moveToNearestBalls() {
        Pose startPos = robot.getFollower().pose();
        Pose endPos = robot.getLimelight().getFinalPose(startPos);
        Path path = line(startPos, endPos).linear(startPos, endPos);
        return instant(() -> robot.getFollower().follow(path));
    }
}
