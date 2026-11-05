//package org.firstinspires.ftc.teamcode.mechanisms.vision;
//
//import org.firstinspires.ftc.teamcode.data.Alliance;
//import org.hivevisionftc.vision.BiobuzzClasses;
//import org.hivevisionftc.vision.Detection;
//import org.hivevisionftc.vision.HiveVision;
//import org.hivevisionftc.vision.VisionConfig;
//
//import java.util.List;
//
//import dev.nextftc.hardware.webcams.NextLimelight;
//import dev.nextftc.robot.Mechanism;
//
//public class Limelight implements Mechanism {
//    private NextLimelight limelight = new NextLimelight("limelight");
//
//    public HiveVision vision = new HiveVision(limelight.getCamera(), new VisionConfig()
//            .cameraHeightIn(12.5)
//            .cameraPitchDeg(-24)
//            .targetHeightIn(3.5));
//
//    public Limelight() {
//        limelight.setPipeline(1);
//        limelight.setPollRate(50);
//    }
//
//    public void startReading() {
//        limelight.startReading(1, 50);
//    }
//
//    public void setAllowedBalls() {
//        if(Alliance.currentAlliance == Alliance.BLUE) {
//            vision.setAllowedClasses(BiobuzzClasses.NECTAR_BLUE, BiobuzzClasses.POLLEN);
//        } else if(Alliance.currentAlliance == Alliance.RED) {
//            vision.setAllowedClasses(BiobuzzClasses.NECTAR_RED, BiobuzzClasses.POLLEN);
//        }
//    }
//
//    @Override
//    public void periodic() {
//
//    }
//}
