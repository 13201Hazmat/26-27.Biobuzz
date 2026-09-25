package org.firstinspires.ftc.teamcode.mechanisms;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector;

import dev.nextftc.hardware.webcams.NextLimelight;
import dev.nextftc.robot.Mechanism;

public class Limelight implements Mechanism {
    private final NextLimelight limelight = new NextLimelight("Limelight");

    private static final double CAMERA_PITCH = 10;
    private static final double POLLEN_BALL_HEIGHT = 3;
    private static final double CAMERA_HEIGHT = 9.5;
    private static final double CAMERA_OFFSET_X = 0;
    private static final double CAMERA_OFFSET_Y = 7.5;
    private static final double CAMERA_POLLEN_DIFFY = CAMERA_HEIGHT - POLLEN_BALL_HEIGHT;

    public Limelight() {
    }

    public void start() {
        limelight.startReading(2, 100);
    }

    public void setTracking() {
        limelight.setPipeline(2);
    }

    public NextLimelight getLimelight() {
        return limelight;
    }

    public Pose getFinalPose(double targetX, double targetY, Pose currentPose) {

        if (currentPose == null) {
            return null;
        }

        double cameraToBallAngle = Math.toRadians(CAMERA_PITCH + targetY);

        // Checks for impossible math
        if (Math.abs(Math.tan(cameraToBallAngle)) < 0.0001) {
            return null;
        }

        double forwardDistance = CAMERA_POLLEN_DIFFY / Math.tan(cameraToBallAngle);
        double lateralDistance = forwardDistance * Math.tan(Math.toRadians(targetX));

        forwardDistance += CAMERA_OFFSET_Y;
        lateralDistance += CAMERA_OFFSET_X;

        Vector robotToBallVector = new Vector(forwardDistance, lateralDistance);

        robotToBallVector.toVector2D().rotate(currentPose.heading());

        double fieldX = currentPose.x() + robotToBallVector.toVector2D().x();

        double fieldY = currentPose.y() + robotToBallVector.toVector2D().y();

        return new Pose(fieldX, fieldY, currentPose.heading());
    }

    public int getAmountBalls() {
        return limelight.getLatestResult().getDetectorResults().size();
    }

    public Pose getFinalPose(Pose currentPose) {
        return getFinalPose(limelight.getTX(), limelight.getTY(), currentPose);
    }
}