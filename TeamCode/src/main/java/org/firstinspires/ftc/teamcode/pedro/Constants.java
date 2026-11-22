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
        c.xPodOffset.set(3.3070866141732287);
        c.yPodOffset.set(-3.385826771653543);
        c.xPodDirection.set(OctoQuad.EncoderDirection.REVERSE);
        c.yPodDirection.set(OctoQuad.EncoderDirection.REVERSE);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.i2cRecoveryMode.set(OctoQuad.I2cRecoveryMode.MODE_1_PERIPH_RST_ON_FRAME_ERR);
        c.headingScalar.set(0.9976848931744839);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.40651827863566536);
                Controller secondaryTranslationalForward = Controller.proportional(0.1501976415874181);
                Controller primaryTranslationalLateral = Controller.proportional(0.6358099354527341);
                Controller secondaryTranslationalLateral = Controller.proportional(0.23491478199541618);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.012500253469647536));
                c.brake.set(Controller.proportionalFeedforward(0.010625215449200405));

                c.headingFeedback.set(Controller.proportional(6.765687129506292));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.05439936105532707, 0.005555652427770621));

                c.linearBrakeCoefficients.set(Matrix.diag(0.08911687833896302, 0.05209918070744638));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.002004033316289177, 0.0020328119956620513));

                c.maxAchievableForwardVelocity.set(75.29405972927806);
                c.maxAchievableStrafeVelocity.set(56.27942794969255);
                c.naturalForwardDeceleration.set(37.20942918878855);
                c.naturalStrafeDeceleration.set(72.80881813282643);
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