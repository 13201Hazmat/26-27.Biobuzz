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

    public static MecanumConfig driveConfig = new MecanumConfig(
            c -> {
                c.frontLeftName.set(Config.fl);
                c.backLeftName.set(Config.bL);
                c.frontRightName.set(Config.fR);
                c.backRightName.set(Config.bR);

                c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
                c.frontRightDirection.set(DcMotorSimple.Direction.FORWARD);
                c.backRightDirection.set(DcMotorSimple.Direction.FORWARD);
            }
    );

    public static OctoQuadConfig octoQuadConfig = new OctoQuadConfig(c -> {
        c.name.set("octoquad");
        c.xPodPort.set(0);
        c.yPodPort.set(1);
        c.ticksPerUnit.set(505.316944406);
        c.xPodOffset.set(-0.15748031496062992);
        c.yPodOffset.set(-2.4015748031496065);
        c.xPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
        c.yPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.i2cRecoveryMode.set(OctoQuad.I2cRecoveryMode.MODE_1_PERIPH_RST_ON_FRAME_ERR);
        c.headingScalar.set(0.9980906104344437);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.288164046166755);
                Controller secondaryTranslationalForward = Controller.proportional(0.1064689151734916);
                Controller primaryTranslationalLateral = Controller.proportional(0.3992830023201202);
                Controller secondaryTranslationalLateral = Controller.proportional(0.14752440031896794);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.012065818450188887));
                c.brake.set(Controller.proportionalFeedforward(0.010255945682660554));

                c.headingFeedback.set(Controller.proportional(4.397159898275782));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.041731348412274764, 0.006553664079208524));

                c.linearBrakeCoefficients.set(Matrix.diag(0.06845360972805715, 0.051432465121919575));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0012892304942403164, 0.0017167109212243258));

                c.maxAchievableForwardVelocity.set(85.84657278638646);
                c.maxAchievableStrafeVelocity.set(72.36370975625626);
                c.naturalForwardDeceleration.set(57.27590320668094);
                c.naturalStrafeDeceleration.set(74.95453985810164);
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