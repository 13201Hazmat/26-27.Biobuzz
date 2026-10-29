package org.firstinspires.ftc.teamcode.opmodes.auto.executable;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.opmodes.auto.setup.BaseAuto;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import dev.nextftc.robot.opmode.NextAutonomous;

@NextAutonomous(name = "Double Flower Auto")
public class DoubleFlowerAuto extends BaseAuto {
    public DoubleFlowerAuto(HazmatRobot robot) {
        super(robot, Alliance.RED);
        setAutoCommand(runnables.doubleFlowerAuto(true));
    }
}
