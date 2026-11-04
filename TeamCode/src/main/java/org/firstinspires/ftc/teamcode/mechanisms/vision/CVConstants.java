package org.firstinspires.ftc.teamcode.mechanisms.vision;

import com.pedropathing.math.Matrix;

public class CVConstants {
    // TODO HEIGHT FROM FLOOR - 1.4
    public static final double CAMERA_HEIGHT = 7.1;
    // TODO DEGREES POINTING DOWNWARDS
    public static final double CAMERA_PITCH = Math.toRadians(1.3);
    // TODO HOW FAR LIMELIGHT IS FROM CENTER OF ROBOT
    public static final double FORWARD_OFFSET = 3; // CHANGE THIS

    public static final Matrix xyzConverter = new Matrix(
            new double[][] {
                    {1, 0, 0},
                    {0, 0, 1},
                    {0, 1, 0},
            });

    public static Matrix createRotationX(double angle) {
        double c = Math.cos(angle);
        double s = Math.sin(angle);
        return new Matrix(
                new double[][] {
                        {1, 0,  0},
                        {0, c, -s},
                        {0, s,  c},
                }
        );
    }

}