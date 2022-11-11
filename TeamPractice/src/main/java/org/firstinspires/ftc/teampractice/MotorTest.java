package org.firstinspires.ftc.teampractice;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "Motor test", group = "Example")
@Disabled
public class MotorTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot();
        robot.init(hardwareMap, false);

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double axial    = -gamepad1.left_stick_y - gamepad1.right_stick_y;  // Note: pushing stick forward gives negative value
            double yaw      =  gamepad1.left_stick_x;
            double lateral  =  gamepad1.right_stick_x;

            double leftFrontPower  = 0d;
            double rightFrontPower = 0d;
            double leftBackPower   = 0d;
            double rightBackPower  = 0d;

            // This is test code:
            //
            // Uncomment the following code to test your motor directions.
            // Each button should make the corresponding motor run FORWARD.
            //   1) First get all the motors to take to correct positions on the robot
            //      by adjusting your Robot Configuration if necessary.
            //   2) Then make sure they run in the correct direction by modifying the
            //      the setDirection() calls above.
            // Once the correct motors move in the correct direction re-comment this code.

            leftFrontPower  = gamepad1.x ? 1d : 0d;  // X gamepad
            leftBackPower   = gamepad1.a ? 1d : 0d;  // A gamepad
            rightFrontPower = gamepad1.y ? 1d : 0d;  // Y gamepad
            rightBackPower  = gamepad1.b ? 1d : 0d;  // B gamepad

            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            leftFrontPower  = leftFrontPower == 0d  ? axial + lateral + yaw : leftFrontPower;
            rightFrontPower = rightFrontPower == 0d ? axial - lateral - yaw : rightFrontPower;
            leftBackPower   = leftBackPower == 0d   ? axial - lateral + yaw : leftBackPower;
            rightBackPower  = rightBackPower == 0d  ? axial + lateral - yaw : rightBackPower;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // Send calculated power to wheels
            robot.setDrivePower(leftFrontPower, leftBackPower, rightFrontPower, rightBackPower);

            telemetry.addData("Motor Encoder", "LF: %d, RF: %d, LR: %d, RR: %d",
                    robot.leftFront.getCurrentPosition(),
                    robot.rightFront.getCurrentPosition(),
                    robot.leftRear.getCurrentPosition(),
                    robot.rightRear.getCurrentPosition());

            telemetry.addData("Motor Velocity", "LF: %4.1f, RF: %4.1f, LR: %4.1f, RR: %4.1f",
                    robot.leftFront.getVelocity(),
                    robot.rightFront.getVelocity(),
                    robot.leftRear.getVelocity(),
                    robot.rightRear.getVelocity());

            telemetry.update();
        }
    }
}
