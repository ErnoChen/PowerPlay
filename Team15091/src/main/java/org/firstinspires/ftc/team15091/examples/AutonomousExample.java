package org.firstinspires.ftc.team15091.examples;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.team15091.AutonomousBase;

@Autonomous(name = "Blue side", group = "Example", preselectTeleOp="Gamepad")
@Disabled
public class AutonomousExample extends AutonomousBase {

    @Override
    public void runOpMode() throws InterruptedException {
        setupAndWait();

        robotDriver.gyroDrive(0.6d, 5d, 0d, 1.5d, null);
        robotDriver.gyroTurn(1d, 90d, 1d);
        robotDriver.gyroSlide(1d, -14d, -90d, 2d, null);
    }
}
