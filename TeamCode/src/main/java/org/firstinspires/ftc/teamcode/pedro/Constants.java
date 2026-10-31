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
        c.xPodPort.set(1);
        c.yPodPort.set(0);
        c.ticksPerUnit.set(505.316944406);
        c.xPodOffset.set(0.7480314960629921);
        c.yPodOffset.set(0.4330708661417323);
        c.xPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
        c.yPodDirection.set(OctoQuad.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
        c.i2cRecoveryMode.set(OctoQuad.I2cRecoveryMode.MODE_1_PERIPH_RST_ON_FRAME_ERR);
        c.headingScalar.set(1.891925832163941);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.3029136308485262);
                Controller secondaryTranslationalForward = Controller.proportional(0.11191849259724479);
                Controller primaryTranslationalLateral = Controller.proportional(0.3774394041470068);
                Controller secondaryTranslationalLateral = Controller.proportional(0.13945377446569535);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.013639594058747866));
                c.brake.set(Controller.proportionalFeedforward(0.011593654949935686));

                c.headingFeedback.set(Controller.proportional(3.1837835981796556));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.041159037378540896, 0.0044708457602167795));

                c.linearBrakeCoefficients.set(Matrix.diag(0.0708245883761525, 0.03499417766788811));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0013378662582098793, 0.0020635442675127247));

                c.maxAchievableForwardVelocity.set(84.05401911481054);
                c.maxAchievableStrafeVelocity.set(66.86044275388784);
                c.naturalForwardDeceleration.set(58.435983487719355);
                c.naturalStrafeDeceleration.set(79.48003829772966);
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