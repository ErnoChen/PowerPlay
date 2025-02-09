package org.firstinspires.ftc.team15091;

public abstract class AutonomousBase extends OpModeBase {
    protected RobotDriver robotDriver;
    protected long delay_start = 0;

    final protected void setupAndWait() {
        robot.init(hardwareMap, true);
        robotDriver = new RobotDriver(robot, this);

        telemetry.addData("Heading", "%.4f", robot.getHeading());
        telemetry.addData("Delay", "%d", delay_start);

        // Wait for the game to start (driver presses PLAY)
        // Abort this loop is started or stopped.
        while (!(isStarted() || isStopRequested())) {
            if (gamepad1.a || gamepad2.a) {
                if (!a_pressed) {
                    // a is pressed
                }
            }

            if (gamepad1.dpad_up || gamepad2.dpad_up) {
                if (!dpad_pressed) {
                    delay_start += 100;
                }
            } else if (gamepad1.dpad_down || gamepad2.dpad_down) {
                if (!dpad_pressed) {
                    if (delay_start > 0) {
                        delay_start -= 100;
                    }
                }
            }

            gamepadUpdate();

            telemetry.update();
            idle();
        }

        if (delay_start > 0d) {
            sleep(delay_start);
        }
    }
}
