package org.firstinspires.ftc.team15091;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name = "Gamepad", group = "Linear Opmode")
public class GamepadOpMode extends OpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap, false);

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double drive = Math.tan((-gamepad1.left_stick_y - gamepad1.right_stick_y) * Math.tan(1d));
            double turn = Math.atan(gamepad1.left_stick_x * Math.tan(0.6d));
            double side = Math.atan(gamepad1.right_stick_x * Math.tan(1d));
            if (gamepad1.left_trigger > 0.2) {
                if (gamepad1.left_bumper) {
                    robot.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    robot.armMotor.setPower(gamepad1.left_trigger / 2);
                }
                //robot.armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.armMotor.setTargetPosition(3000);
                robot.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armMotor.setPower(gamepad1.left_trigger);
            }
            else if (gamepad1.right_trigger > 0.2) {
                if (gamepad1.right_bumper) {
                    robot.armMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
                    robot.armMotor.setPower(-gamepad1.right_trigger / 2);
                }
                //robot.armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                robot.armMotor.setTargetPosition(0);
                robot.armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                robot.armMotor.setPower(gamepad1.right_trigger);
            }
            else {
                robot.armMotor.setPower(0);
            }
            if ((!gamepad1.left_bumper && lb_pressed) || (!gamepad1.right_bumper && rb_pressed)) {
                robot.armMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            }

            if (gamepad1.a) {
                robot.grabberServo.setPosition(0);
            }
            else if (gamepad1.b) {
                robot.grabberServo.setPosition(1);
            }

            double pLeftFront = Range.clip(drive + turn + side, -1.0d, 1.0d);
            double pLeftRear = Range.clip(drive + turn - side, -1.0d, 1.0d);
            double pRightFront = Range.clip(drive - turn - side, -1.0d, 1.0d);
            double pRightRear = Range.clip(drive - turn + side, -1.0d, 1.0d);

            // Send calculated power to wheels
            robot.setDrivePower(pLeftFront, pLeftRear, pRightFront, pRightRear);

            gamepadUpdate();

            telemetry.addData("Motor", "LF: %.2f, RF: %.2f, LR: %.2f, RR: %.2f",
                    robot.leftFront.getVelocity(),
                    robot.rightFront.getVelocity(),
                    robot.leftRear.getVelocity(),
                    robot.rightRear.getVelocity());

            telemetry.update();
        }
    }
}