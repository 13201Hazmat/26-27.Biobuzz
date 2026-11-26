package org.firstinspires.ftc.teamcode.mechanisms.vision;

import android.util.Size;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.data.Config;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;
import org.firstinspires.ftc.vision.apriltag.AprilTagSingleDetection;

import java.util.ArrayList;
import java.util.List;

import dev.nextftc.hardware.RobotController;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.telemetry.Telemetry;

public class Arducam implements Mechanism {
    private VisionPortal visionPortal;
    private AprilTagProcessor processor;
    private List<AprilTagDetection> detectedTags = new ArrayList<>();
    public Arducam() {
        processor = new AprilTagProcessor.Builder()
                .setDrawTagID(true)
                .setDrawTagOutline(true)
                .setDrawAxes(true)
                .setDrawCubeProjection(true)
                .setOutputUnits(DistanceUnit.CM, AngleUnit.DEGREES)
                .build();

        visionPortal = new VisionPortal.Builder()
                .setCamera(RobotController.hardwareMap().get(WebcamName.class, Config.arducamName))
                .setCameraResolution(new Size(640, 480))
                .addProcessor(processor)
                .build();
    }

    public List<AprilTagDetection> getDetectedTags() {
        return this.detectedTags;
    }

    public AprilTagDetection getTagBySpecificId(int id) {
        for(AprilTagDetection detection: this.detectedTags) {
            if(detection instanceof AprilTagSingleDetection) {
                AprilTagSingleDetection singleDet = (AprilTagSingleDetection) detection;
                if(id == singleDet.id) {
                    return detection;
                }
            }
        }
        return null;
    }

    public void update() {
        this.detectedTags = processor.getFreshDetections();
    }

    public void stop() {
        if(visionPortal != null) {
            visionPortal.close();
        }
    }

    public void debug() {
        AprilTagSingleDetection singleDet = (AprilTagSingleDetection) this.detectedTags.get(0);
        Telemetry.log("Tag ID", singleDet.id);
        Telemetry.log("X pos", singleDet.ftcPose.x);
        Telemetry.log("Y pos", singleDet.ftcPose.y);
        Telemetry.log("Pitch", singleDet.ftcPose.pitch);
    }

    @Override
    public void periodic() {
        update();
    }
}