package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Launcher;
import org.firstinspires.ftc.teamcode.mechanisms.Limelight;
import org.firstinspires.ftc.teamcode.mechanisms.Transfer;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
    private Follower follower;
    private Intake intake;
    private Launcher launcher;
    private Transfer transfer;
    private Limelight limelight;

    public HazmatRobot() {
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

    public Limelight getLimelight(){
        return limelight;
    }

    @NonNull
    @Override
    public Set<Mechanism> getMechanisms() {
        return Set.of(intake, transfer, launcher, limelight);
    }
}