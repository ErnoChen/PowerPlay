package org.firstinspires.ftc.team15091.examples;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.MotorControlAlgorithm;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;
import org.firstinspires.ftc.team15091.OpModeBase;

@TeleOp(name = "Lift Test", group = "Example")
public class LiftUpDown extends OpModeBase {
    @Override
    public void runOpMode() throws InterruptedException {
        boolean dpad_down_pressed = false;

        DcMotorEx liftMotor = hardwareMap.get(DcMotorEx.class, "arm_motor");
        liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        liftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        liftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        liftMotor.setZeroPowerBehavior(DcMotorEx.ZeroPowerBehavior.BRAKE);
        liftMotor.setCurrentAlert(1d, CurrentUnit.AMPS);
        liftMotor.setPositionPIDFCoefficients(5d);
        liftMotor.setTargetPositionTolerance(2);

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.addData("target", String.format("tol: %d",
                        liftMotor.getTargetPositionTolerance()));
        telemetry.addData("pidf",
                liftMotor.getPIDFCoefficients(DcMotor.RunMode.RUN_TO_POSITION).toString());
        telemetry.addData("over current",
                String.format("%.2f", liftMotor.getCurrentAlert(CurrentUnit.AMPS)));
        telemetry.addData("lift", () ->
                String.format("pos: %d, cur: %.2fA",
                        liftMotor.getCurrentPosition(),
                        liftMotor.getCurrent(CurrentUnit.AMPS)));
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.left_trigger > 0d) {
                liftMotor.setTargetPosition(12000);
                if (liftMotor.getMode() != DcMotorEx.RunMode.RUN_TO_POSITION) {
                    liftMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                }
                liftMotor.setPower(gamepad1.left_trigger);
            } else if (gamepad1.right_trigger > 0d) {
                liftMotor.setTargetPosition(0);
                if (liftMotor.getMode() != DcMotorEx.RunMode.RUN_TO_POSITION) {
                    liftMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                }
                liftMotor.setPower(gamepad1.right_trigger);
            } else {
                if (gamepad1.dpad_up) {
                    if (liftMotor.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
                        liftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
                    }

                    liftMotor.setPower(0.2d);
                } else if (gamepad1.dpad_down) {
                    if (liftMotor.getMode() != DcMotorEx.RunMode.RUN_USING_ENCODER) {
                        liftMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
                    }
                    liftMotor.setPower(-0.2d);
                } else {
                    liftMotor.setPower(0d);
                }
            }

            if (dpad_down_pressed == true && gamepad1.dpad_down == false) {
                liftMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
            }

            dpad_down_pressed = gamepad1.dpad_down;

            gamepadUpdate();
            telemetry.update();
        }
    }
}
