package org.firstinspires.ftc.team15091.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.team15091.AutonomousBase;

@Autonomous(name = "Mean Color", group = "Example", preselectTeleOp="Gamepad")
@Disabled
public class MeanColor extends AutonomousBase {
    MeanColorPipeline pipeline;
    @Override
    public void runOpMode() throws InterruptedException {
        pipeline = new MeanColorPipeline();
        pipeline.setupWebcam(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Analysis", () -> pipeline.data);

        // Wait for the game to start (driver presses PLAY)
        // Abort this loop is started or stopped.
        while (!(isStarted() || isStopRequested())) {
            telemetry.update();
            idle();
        }

        pipeline.webcam.stopStreaming();
    }
}
