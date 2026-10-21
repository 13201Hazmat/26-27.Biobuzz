package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.tuning.autotune.Procedure;

import org.firstinspires.ftc.teamcode.pedro.procedures.MecanumTuner;

public class Tuning {

    //    @Tuner
    public static Procedure mecanumTuner() {
        return new MecanumTuner();
    }

//    @Tuner
//    public static Procedure octoquadTuner() {
//        return new OctoQuadTuner();
//    }
//
//    @Tuner
//    public static Procedure foresightTuner() {
//        return new ForesightTuner((hardwareMap) -> new OctoQuadLocalizer(hardwareMap, Constants.octoQuadConfig), (hardwareMap) -> new Mecanum(hardwareMap, Constants.driveConfig));
//    }
}
