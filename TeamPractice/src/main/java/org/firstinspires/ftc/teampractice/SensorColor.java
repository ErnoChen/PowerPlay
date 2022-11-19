package org.firstinspires.ftc.teampractice;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Sensor color", group = "Example")
public class SensorColor extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        ColorRangeSensor sensorRangeColor = hardwareMap.get(ColorRangeSensor.class, "sensor_color");
        boolean a_pressed = false, b_pressed = false;
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.addData("color", () ->
                String.format("%3d %3d %3d", sensorRangeColor.red(), sensorRangeColor.green(), sensorRangeColor.blue()));
        telemetry.addData("range", () ->
                String.format("%.1f", sensorRangeColor.getDistance(DistanceUnit.CM)));
        telemetry.addData("gain", () ->
                String.format("%.3f", sensorRangeColor.getGain()));
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.a && !a_pressed) {
                float gain = sensorRangeColor.getGain();
                gain += 0.005;
                sensorRangeColor.setGain(gain);
            }
            if (gamepad1.b && !b_pressed) {
                float gain = sensorRangeColor.getGain();
                gain -= 0.005;
                sensorRangeColor.setGain(gain);
            }
            a_pressed = gamepad1.a;
            b_pressed = gamepad1.b;
            telemetry.update();
        }
    }
}