package org.firstinspires.ftc.team15091;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name = "Gamepad", group = "Linear Opmode")
public class GamepadOpMode extends OpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        boolean isEncoderReset = false, listeningForButtonPress = false, holdPosition = false;
        ElapsedTime runtime = new ElapsedTime();
        int highPolePos = 1950, mediumPolePos = 1600, lowPolePos = 950, junctionPos = 200, currentTarget = 0;
        robot.init(hardwareMap, false);

        //region telemetry setup
        telemetry.addData(">", "Press Play to start op mode");
        telemetry.addData("lift", () -> String.format("pos: %d, cur: %.2fA", robot.armMotor.getCurrentPosition(), robot.armMotor.getCurrent(CurrentUnit.AMPS)));
        telemetry.addData("Motor", () -> String.format("LF: %.2f, RF: %.2f, LR: %.2f, RR: %.2f", robot.leftFront.getVelocity(), robot.rightFront.getVelocity(), robot.leftRear.getVelocity(), robot.rightRear.getVelocity()));
        telemetry.update();
        //endregion

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //region arm control
            int currentArmPosition = robot.armMotor.getCurrentPosition();

//            if (gamepad1.start && !start_pressed) {
//                isEncoderReset = !isEncoderReset;
//                robot.setArmMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
//            }

            if (gamepad1.left_trigger > 0d) { // raise lift
                robot.armMotor.setTargetPosition(highPolePos);
                robot.setArmMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armMotor.setPower(gamepad1.left_trigger);
                currentTarget = currentArmPosition;
            } else if (gamepad1.right_trigger > 0d && // lower lift
                    robot.limitSwitch.getState() == true) { // when limit sensor not pressed
                double powerScale = currentArmPosition > 800 ? 1d : 0.4d;
                robot.setArmMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
                robot.armMotor.setPower(-gamepad1.right_trigger * powerScale);
                currentTarget = currentArmPosition;
                holdPosition = false;
            } else { // stop lift
                if (holdPosition == false && robot.limitSwitch.getState() == false) { // limit sensor pressed
                    robot.setArmMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    currentTarget = 0;
                } else {
                    robot.armMotor.setTargetPosition(currentTarget);
                    robot.setArmMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    robot.armMotor.setPower(0.8d);
                }
            }

            if (gamepad1.left_bumper && !lb_pressed) {
                runtime.reset();
                listeningForButtonPress = true; // toggle auto arm to position
                currentTarget = highPolePos;
                holdPosition = true;
            } else if (gamepad1.right_bumper && !rb_pressed) {
                runtime.reset();
                currentTarget = -10;
                holdPosition = false;
            }

            if (listeningForButtonPress) {
                if (gamepad1.b || gamepad1.x || gamepad1.y) {
                    listeningForButtonPress = false;
                    if (gamepad1.b) {
                        currentTarget = lowPolePos;
                    } else if (gamepad1.y) {
                        currentTarget = mediumPolePos;
                    } else if (gamepad1.x) {
                        currentTarget = junctionPos;
                    }
                }
                if (runtime.milliseconds() > 500) {
                    listeningForButtonPress = false;
                }
            }
            //endregion

            //region grabber servo control
            if (gamepad1.a) {
                // close grabber
                robot.setGrabber(1d);
            } else if (gamepad1.b && !listeningForButtonPress) {
                // open grabber
                robot.setGrabber(0d);
            }
            //endregion

            //region drivetrain control
            double drive = Math.tan((-gamepad1.left_stick_y - gamepad1.right_stick_y) * Math.tan(1d));
            double turn = Math.atan(gamepad1.left_stick_x * Math.tan(0.6d));
            double side = Math.atan(gamepad1.right_stick_x * Math.tan(1d));

            double pLeftFront = Range.clip(drive + turn + side, -1.0d, 1.0d);
            double pLeftRear = Range.clip(drive + turn - side, -1.0d, 1.0d);
            double pRightFront = Range.clip(drive - turn - side, -1.0d, 1.0d);
            double pRightRear = Range.clip(drive - turn + side, -1.0d, 1.0d);

            // Send calculated power to wheels
            robot.setDrivePower(pLeftFront, pLeftRear, pRightFront, pRightRear);
            //endregion

            gamepadUpdate();
            telemetry.update();
        }
    }
}