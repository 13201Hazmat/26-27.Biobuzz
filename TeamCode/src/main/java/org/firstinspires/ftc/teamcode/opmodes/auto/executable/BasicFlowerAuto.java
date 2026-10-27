package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.HazmatAuto;
import static org.firstinspires.ftc.teamcode.opmodes.auto.setup.PathsAndPoses.*;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

public class BasicFlowerAuto extends HazmatAuto {
    protected BasicFlowerAuto(HazmatRobot robot) {
        super(robot, Alliance.RED, BasicFlowerAutoPoses.startingPose);
    }
}
