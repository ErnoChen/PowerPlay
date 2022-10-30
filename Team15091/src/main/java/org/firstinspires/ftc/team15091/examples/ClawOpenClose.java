package org.firstinspires.ftc.team15091.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.team15091.OpModeBase;

@TeleOp(name = "Claw", group = "Example")
public class ClawOpenClose extends OpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        Servo clawServo = hardwareMap.servo.get("grabber_servo");

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.addData("claw", () -> String.format("pos: %.1f", clawServo.getPosition()));
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (a_pressed != gamepad1.a) {
                clawServo.setPosition(1d);
            }

            if (b_pressed != gamepad1.b) {
                clawServo.setPosition(0d);
            }

            gamepadUpdate();
            telemetry.update();
        }
    }
}
