package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.OctoQuadConfig;
import com.pedropathing.revhub.localizers.OctoQuadLocalizer;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {

    public static MecanumConfig driveConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeft");
        c.frontRightName.set("frontRight");
        c.backLeftName.set("backLeft");
        c.backRightName.set("backRight");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });


    public static OctoQuadConfig octoQuadConfig = new OctoQuadConfig(c -> {
        c.name.set("octoquad");
        c.xPodPort.set(1);
        c.yPodPort.set(0);
        c.ticksPerUnit.set(505.316944406);
        c.xPodOffset.set(-0.45275590551181105);
        c.yPodOffset.set(-1.3188976377952757);
        c.xPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
        c.yPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.i2cRecoveryMode.set(OctoQuad.I2cRecoveryMode.MODE_1_PERIPH_RST_ON_FRAME_ERR);
        c.headingScalar.set(1.333819966281649);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3029382512827203);
                Controller secondaryTranslationalForward = Controller.proportional(0.1119275891898094);
                Controller primaryTranslationalLateral = Controller.proportional(0.3506794014346132);
                Controller secondaryTranslationalLateral = Controller.proportional(0.12956666850390747);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013147444777074772));
                c.brake.set(Controller.proportionalFeedforward(0.011175328060513556));

                c.headingFeedback.set(Controller.proportional(3.4258792675221477));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.04305162293350962, 0.005299362540377597));

                c.linearBrakeCoefficients.set(Matrix.diag(0.05468693236642857, 0.028732255080075295));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0020017979995227793, 0.002403885268830715));

                c.maxAchievableForwardVelocity.set(79.68314786152564);
                c.maxAchievableStrafeVelocity.set(58.63458033182726);
                c.naturalForwardDeceleration.set(52.93299499617701);
                c.naturalStrafeDeceleration.set(72.35791827853495);
            }
    );

    public static Follower create(HardwareMap h) {
        return new Follower(
                new OctoQuadLocalizer(h, octoQuadConfig),
                new Mecanum(h, driveConfig),
                new Foresight(foresightConfig)
        );
    }
}