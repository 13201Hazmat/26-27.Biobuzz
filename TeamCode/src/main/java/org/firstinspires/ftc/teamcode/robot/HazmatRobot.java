package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
    private Follower follower;
    private Intake intake;

    public HazmatRobot() {
        intake = new Intake();
    }

    public Follower getFollower() {
        if (follower == null) {
            follower = Constants.create(RobotController.hardwareMap());
        }
        return follower;
    }

    public Intake getIntake() {
        return intake;
    }

    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(intake);
    }
}