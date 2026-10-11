package org.firstinspires.ftc.teamcode.robot;

import androidx.annotation.NonNull;

import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.mechanisms.Turret;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.Set;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.NextRobot;

public class HazmatRobot implements NextRobot {
	private Follower follower;
	private Intake intake;
	private Turret turret;

	public HazmatRobot() {
		intake = new Intake();
		turret = new Turret(RobotController.hardwareMap());
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

	public Turret getTurret(){
		return turret;
	}
	@Override
	public Set<Mechanism> getMechanisms() {
		return Set.of(intake, turret);
	}
}