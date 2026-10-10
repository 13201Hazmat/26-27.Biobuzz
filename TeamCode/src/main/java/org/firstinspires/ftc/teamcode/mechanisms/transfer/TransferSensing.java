package org.firstinspires.ftc.teamcode.mechanisms.transfer;

import org.firstinspires.ftc.teamcode.data.BallType;
import org.firstinspires.ftc.teamcode.data.ColorProfiles;

import java.util.ArrayDeque;
import java.util.NoSuchElementException;
import java.util.Queue;

import dev.nextftc.hardware.sensors.NextColorDistanceSensor;
import dev.nextftc.hardware.sensors.NextDigitalSensor;
import dev.nextftc.robot.Mechanism;
import dev.nextftc.robot.Telemetry;

public class TransferSensing implements Mechanism {

    private final NextDigitalSensor transferBeamBreak = new NextDigitalSensor("beamBreak");
    private final NextColorDistanceSensor transferColorSensor = new NextColorDistanceSensor("colorSensor");

    private final ArrayDeque<BallType> storedBalls;

    public TransferSensing() {
        this.storedBalls = new ArrayDeque<>();
    }

    public float[] getHSV() {
        return transferColorSensor.getColor().getHsv();

    }

    public BallType getBallType() {
        if (transferColorSensor.isColor(ColorProfiles.POLLEN_COLOR_PROFILE)) {
            return BallType.POLLEN;
        } else if (transferColorSensor.isColor(ColorProfiles.BLUE_NECTAR_COLOR_PROFILE)) {
            return BallType.BLUE_NECTAR;
        } else if (transferColorSensor.isColor(ColorProfiles.RED_NECTAR_COLOR_PROFILE)) {
            return BallType.RED_NECTAR;
        }

        return BallType.NOTHING;
    }

    public NextColorDistanceSensor getTransferColorSensor() {
        return transferColorSensor;
    }

    public NextDigitalSensor getTransferBeamBreak() {
        return transferBeamBreak;
    }

    public Queue<BallType> getStoredBalls() {
        return storedBalls;
    }

    public BallType getTopBall() {
        return storedBalls.peek();
    }

    public void removeTopBall() {
        try {
            storedBalls.removeFirst();
        } catch (NoSuchElementException e) {
            // bl
        }
    }

    public boolean isFull() {
        return storedBalls.size() == 4;
    }

    public void init() {
        storedBalls.clear();
    }

    public void debug() {
        Telemetry.log("DEBUG", transferColorSensor.debug());
        Telemetry.log("BALL TYPE", BallType.currentBallType);
        Telemetry.log("NUMBER OF BALLS", storedBalls.size());
        Telemetry.log("QUEUE:", storedBalls);
    }



    @Override
    public void periodic() {
        transferColorSensor.update();
        BallType.currentBallType = getBallType();
    }
}
