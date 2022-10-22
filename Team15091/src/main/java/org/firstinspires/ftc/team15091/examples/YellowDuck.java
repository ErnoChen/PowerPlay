package org.firstinspires.ftc.team15091.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.team15091.AutonomousBase;

@Autonomous(name = "Yellow Duck", group = "Example", preselectTeleOp="Gamepad")
@Disabled
public class YellowDuck extends AutonomousBase {
    YellowDuckPipeline pipeline;
    @Override
    public void runOpMode() throws InterruptedException {
        pipeline = new YellowDuckPipeline();
        pipeline.setupWebcam(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Analysis", () -> pipeline.data);
        telemetry.addData("Pipeline", () -> String.format("%d,%d", pipeline.threshold1, pipeline.threshold2));
        // Wait for the game to start (driver presses PLAY)
        // Abort this loop is started or stopped.
        while (!(isStarted() || isStopRequested())) {
            if (gamepad1.a) {
                if (!a_pressed) {
                    pipeline.threshold1--;
                }
            }

            if (gamepad1.b) {
                if (!b_pressed) {
                    pipeline.threshold1++;
                }
            }

            if (gamepad1.x) {
                if (!x_pressed) {
                    pipeline.threshold2--;
                }
            }

            if (gamepad1.y) {
                if (!y_pressed) {
                    pipeline.threshold2++;
                }
            }

            gamepadUpdate();
            telemetry.update();
            idle();
        }

        pipeline.webcam.stopStreaming();
    }
}
