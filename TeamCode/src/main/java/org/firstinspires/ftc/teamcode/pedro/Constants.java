package org.firstinspires.ftc.teamcode.pedro;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.paths.Path;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
import com.pedropathing.revhub.localizers.OctoQuadConfig;
import com.pedropathing.revhub.localizers.OctoQuadLocalizer;
import com.qualcomm.hardware.digitalchickenlabs.OctoQuad;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.data.Config;

public class Constants {

    public static MecanumConfig driveConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("frontLeft");
        c.frontRightName.set("frontRight");
        c.backLeftName.set("backLeft");
        c.backRightName.set("backRight");
        c.frontLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
    });

    public static OctoQuadConfig octoQuadConfig = new OctoQuadConfig(c -> {
        c.name.set("octoquad");
        c.xPodPort.set(1);
        c.yPodPort.set(0);
        c.ticksPerUnit.set(505.316944406);
        c.xPodOffset.set(1.1220472440944882);
        c.yPodOffset.set(-5.019685039370079);
        c.xPodDirection.set(OctoQuad.EncoderDirection.REVERSE);
        c.yPodDirection.set(OctoQuad.EncoderDirection.REVERSE);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.i2cRecoveryMode.set(OctoQuad.I2cRecoveryMode.MODE_1_PERIPH_RST_ON_FRAME_ERR);
        c.headingScalar.set(0.9866833865152973);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.4039264858393892);
                Controller secondaryTranslationalForward = Controller.proportional(0.14924004340317307);
                Controller primaryTranslationalLateral = Controller.proportional(0.6909428669931517);
                Controller secondaryTranslationalLateral = Controller.proportional(0.2552849270205379);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013025162183192333));
                c.brake.set(Controller.proportionalFeedforward(0.011071387855713483));

                c.headingFeedback.set(Controller.proportional(6.843523594005509));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.0559068902338866, 0.005256384539880836));

                c.linearBrakeCoefficients.set(Matrix.diag(0.10844745512521088, 0.053556259569217786));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.001489202209419616, 0.0021510095277678673));

                c.maxAchievableForwardVelocity.set(74.43926122986761);
                c.maxAchievableStrafeVelocity.set(60.64180821787737);
                c.naturalForwardDeceleration.set(30.071941899854643);
                c.naturalStrafeDeceleration.set(70.1905188927601);
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