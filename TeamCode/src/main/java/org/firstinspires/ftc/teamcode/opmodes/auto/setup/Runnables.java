package org.firstinspires.ftc.teamcode.opmodes.auto.setup;

import static com.pedropathing.ivy.commands.Commands.conditional;
import static com.pedropathing.ivy.commands.Commands.instant;
import static com.pedropathing.ivy.commands.Commands.lazy;
import static com.pedropathing.ivy.commands.Commands.waitMs;
import static com.pedropathing.ivy.commands.Commands.waitUntil;
import static com.pedropathing.ivy.groups.Groups.parallel;
import static com.pedropathing.ivy.groups.Groups.sequential;

import com.pedropathing.ivy.Command;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.data.Alliance;
import org.firstinspires.ftc.teamcode.robot.HazmatRobot;

import java.util.function.BooleanSupplier;

public class Runnables {

	private final HazmatRobot robot;
	public final PathsAndPoses paths;

	public Runnables(HazmatRobot robot) {
		this.robot = robot;
		this.paths = new PathsAndPoses();
	}

	/**
	 * Defers path selection until runtime execution using lazy(...). Dynamically
	 * evaluates condition at runtime and executes only the selected path command.
	 */
	public Command decidePath(BooleanSupplier condition, Path option1, Path option2) {
		return lazy(() -> condition.getAsBoolean() ? robot.moveToPoint(option1) : robot.moveToPoint(option2));
	}

	public Command move(Path path) {
		return robot.moveToPoint(path);
	}

	public Command intake(Path path) {
		return sequential(
			parallel(robot.moveToPoint(path), robot.getIntake().setForward()),
			waitMs(1000),
			robot.getIntake().setOff()
		);
	}

	/* FRONT AUTOS (We are shooting first) */

	public Command launch() {
		return sequential(
				waitUntil(()->robot.getLauncher().isReady()).raceWith(waitMs(1000)),
				robot.launch(),
				waitMs(100));
	}

	public Command leaveAuto(BooleanSupplier isFront) {
		return conditional(
			isFront,
			move(paths.leavePoses.startingPose_To_frontLeavePose()),
			move(paths.leavePoses.startingPose_To_backLeavePose())
		);
	}

	public Command preloadAuto(BooleanSupplier isFront, BooleanSupplier goThroughMiddle) {
		return sequential(
			instant(() -> robot.getTurret().setTurretAngle(67)),
			waitMs(1000),
			robot.launch(),
			waitMs(1000),
			intake(paths.cp.startingPose_To_gardenIntakePose()),
			launch(),
			conditional(
				goThroughMiddle,
				// if true go through the middle
				sequential(
					move(paths.leavePoses.midLeavePoses.midPose_To_midLeavePose()),
					decidePath(
						isFront,
						paths.leavePoses.midLeavePoses.midLeavePose_To_frontLeavePose(),
						paths.leavePoses.midLeavePoses.midLeavePose_To_backLeavePose()
					)
				),
				// if false dont go through middle
				sequential(
					decidePath(
						isFront,
						paths.leavePoses.midPose_To_frontLeavePose(),
						paths.leavePoses.midPose_To_backLeavePose()
					)
				)
			)
		);
	}
	public Command closeFlowerAuto(BooleanSupplier isFront, BooleanSupplier goThroughMiddle) {
		return sequential(
			// robot.launch(),
			intake(paths.cp.startingPose_To_gardenIntakePose()),
			launch(),
			intake(paths.closeFlowerPoses.gardenIntakePose_To_beforeCloseFlowerIntakePose()),
			robot.getIntake().setRampDown(),
			intake(paths.closeFlowerPoses.beforeCloseFlowerIntakePose_To_afterCloseFlowerIntakePose()),
			conditional(
				goThroughMiddle,
				sequential(
					intake(paths.closeFlowerPoses.middlePoses.afterCloseFlowerIntakePose_To_midMiddlePathPose()),
					robot.getIntake().setRampUp(),
					intake(paths.closeFlowerPoses.middlePoses.midMiddlePathPose_To_secondMidMiddlePathPose()),
					// launch(),
					decidePath(
						isFront, paths.closeFlowerPoses.middlePoses.secondMidMiddlePathPose_To_frontLeavePose(),
						paths.closeFlowerPoses.middlePoses.secondMidMiddlePathPose_To_backLeavePose()
					)
				),
				sequential(
					waitMs(100),
					decidePath(
						isFront, paths.closeFlowerPoses.afterCloseFlowerIntakePose_To_frontLeavePose(),
						paths.closeFlowerPoses.afterCloseFlowerIntakePose_To_backLeavePose()
					), robot.getIntake().setRampUp()

					// launch()
				)
			)
		);
	}

	public Command farFlowerAuto(BooleanSupplier isFront, BooleanSupplier goThroughMiddle) {
		return sequential(
			launch(),
			intake(paths.cp.startingPose_To_gardenIntakePose()),
			launch(),
			conditional(
				goThroughMiddle,
				sequential(
					intake(paths.farFlowerPoses.middlePoses.gardenIntakePose_To_midMiddlePose()),
					intake(paths.farFlowerPoses.middlePoses.midMiddlePose_To_beforeFarFlowerIntakePose()),
					launch(),
					intake(paths.farFlowerPoses.beforeFarFlowerIntakePose_To_afterFarFlowerIntakePose()),
					decidePath(
						isFront, paths.farFlowerPoses.afterFarFlowerIntakePose_To_frontLeavePose(),
						paths.farFlowerPoses.afterFarFlowerIntakePose_To_backLeavePose()
					)
				),
				sequential(
					intake(paths.farFlowerPoses.gardenIntakePose_To_midPose()),
					intake(paths.farFlowerPoses.midPose_To_beforeFarFlowerIntakePose()),
					launch(),
					intake(paths.farFlowerPoses.beforeFarFlowerIntakePose_To_afterFarFlowerIntakePose()),
					decidePath(
						isFront, paths.farFlowerPoses.afterFarFlowerIntakePose_To_frontLeavePose(),
						paths.farFlowerPoses.afterFarFlowerIntakePose_To_backLeavePose()
					)
				)
			)
		);
	}

	public Command doubleFlowerAuto(BooleanSupplier isFront, BooleanSupplier goThroughMiddle) {
		return sequential(
			launch(),
			intake(paths.cp.startingPose_To_gardenIntakePose()),
			launch(),
			intake(paths.closeFlowerPoses.gardenIntakePose_To_beforeCloseFlowerIntakePose()),
			intake(paths.closeFlowerPoses.beforeCloseFlowerIntakePose_To_afterCloseFlowerIntakePose()),
			conditional(
				goThroughMiddle, sequential(
					move(paths.closeFlowerPoses.middlePoses.afterCloseFlowerIntakePose_To_midMiddlePathPose()),
					move(paths.closeFlowerPoses.middlePoses.midMiddlePathPose_To_secondMidMiddlePathPose()),
					move(paths.doubleFlowerPoses.middlePoses.secondMidMiddlePose_To_beforeFarFlowerIntakePose()),
					launch(),
					move(paths.farFlowerPoses.beforeFarFlowerIntakePose_To_afterFarFlowerIntakePose()),
					launch(),
					decidePath(
						isFront,
						paths.farFlowerPoses.afterFarFlowerIntakePose_To_frontLeavePose(),
						paths.farFlowerPoses.afterFarFlowerIntakePose_To_backLeavePose()
					)
				),
				sequential(
					move(paths.doubleFlowerPoses.afterCloseFlowerIntakePose_To_beforeFarFlowerIntakePose()),
					launch(),
					move(paths.farFlowerPoses.beforeFarFlowerIntakePose_To_afterFarFlowerIntakePose()),
					launch(),
					decidePath(
						isFront,
						paths.farFlowerPoses.afterFarFlowerIntakePose_To_frontLeavePose(),
						paths.farFlowerPoses.afterFarFlowerIntakePose_To_backLeavePose()
					)
				)
			)
		);
	}

	/* BACK AUTOS (We are waiting first) */

	public Command backLeaveAuto(BooleanSupplier isFront) {
		return sequential(
			waitMs(100),
			decidePath(
				isFront,
				paths.farFlowerPoses.afterFarFlowerIntakePose_To_frontLeavePose(),
				paths.farFlowerPoses.afterFarFlowerIntakePose_To_backLeavePose()
			)
		);
	}

	public Command backFarFlowerAuto(BooleanSupplier isFront) {
		return sequential(
			waitMs(100),
			intake(paths.backLeaveAutoPoses.backStartingPose_To_beforeFarFlowerIntakePose()),
			waitMs(100),
			intake(paths.farFlowerPoses.beforeFarFlowerIntakePose_To_afterFarFlowerIntakePose()),
			waitMs(100),
			decidePath(
				isFront,
				paths.farFlowerPoses.afterFarFlowerIntakePose_To_frontLeavePose(),
				paths.farFlowerPoses.afterFarFlowerIntakePose_To_backLeavePose()
			)
		);
	}

}