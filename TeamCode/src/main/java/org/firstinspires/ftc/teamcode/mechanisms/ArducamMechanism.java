package org.firstinspires.ftc.teamcode.mechanisms;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortalImpl;

import dev.nextftc.robot.Mechanism;

public class ArducamMechanism implements Mechanism {

    private VisionPortal arducam;
    public ArducamMechanism() {
        arducam = new VisionPortal
                .Builder().setCamera()
    }

    @Override
    public void periodic() {

    }
}
