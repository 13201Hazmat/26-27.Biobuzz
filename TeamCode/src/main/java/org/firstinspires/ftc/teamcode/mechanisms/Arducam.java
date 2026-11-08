package org.firstinspires.ftc.teamcode.mechanisms;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseFtc;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class Arducam implements Mechanism {
    private AprilTagProcessor processor;
    private VisionPortal arducam;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();

    public Arducam() {
        this("Webcam 1");
    }

    public Arducam(String cameraName) {
        processor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.INCH, AngleUnit.DEGREES)
                .build();

        VisionPortal.Builder builder = new VisionPortal.Builder();
        builder.setCamera(RobotController.hardwareMap().get(WebcamName.class, cameraName));
        builder.setCameraResolution(new Size(640, 480));
        builder.addProcessor(processor);

        arducam = builder.build();
    }

    public List<AprilTagDetection> getDetectedTags() {
        return detectedTags;
    }

    public boolean seesAnyTag() {
        return detectedTags != null && !detectedTags.isEmpty();
    }

    public int getDetectedTagCount() {
        return (detectedTags != null) ? detectedTags.size() : 0;
    }

    public AprilTagDetection getFirstDetectedTag() {
        return seesAnyTag() ? detectedTags.get(0) : null;
    }

    public static int getTagId(AprilTagDetection detection) {
        if (detection instanceof AprilTagSingleDetection) {
            return ((AprilTagSingleDetection) detection).id;
        }
        return -1;
    }

    public boolean isTagDetected(int targetId) {
        return getTagById(targetId) != null;
    }

    public AprilTagDetection getTagById(int targetId) {
        for (AprilTagDetection detection : detectedTags) {
            if (getTagId(detection) == targetId) {
                return detection;
            }
        }
        return null;
    }

    private AprilTagPoseFtc getFtcPose(int targetId) {
        AprilTagDetection detection = getTagById(targetId);
        return (detection != null) ? detection.ftcPose : null;
    }

    public double getDistanceToTag(int targetId) {
        AprilTagPoseFtc pose = getFtcPose(targetId);
        return (pose != null) ? pose.range : -1.0;
    }

    public double getPitchToTag(int targetId) {
        AprilTagPoseFtc pose = getFtcPose(targetId);
        return (pose != null) ? pose.pitch : 0.0;
    }

    private double getYawToTag(int targetId) {
        AprilTagPoseFtc pose = getFtcPose(targetId);
        return (pose != null) ? pose.yaw : 0.0;
    }

    public void debug() {
        for (AprilTagDetection detection : detectedTags) {
            int tagId = getTagId(detection);
            if (detection.ftcPose != null) {
                Telemetry.log(
                        String.format(Locale.US, "Tag %d", tagId),
                        String.format(Locale.US, "Dist: %.2fin, Bearing: %.2fdeg, Yaw: %.2fdeg",
                                detection.ftcPose.range,
                                detection.ftcPose.bearing,
                                detection.ftcPose.yaw)
                );

                Telemetry.log("Tag ID", tagId);
                Telemetry.log("Pitch", detection.ftcPose.pitch);
            }
        }
    }

    public void stop() {
        if (arducam != null) {
            arducam.close();
        }
    }

    @Override
    public void periodic() {
        if (processor != null) {
            detectedTags = processor.getDetections();
        }
    }
}