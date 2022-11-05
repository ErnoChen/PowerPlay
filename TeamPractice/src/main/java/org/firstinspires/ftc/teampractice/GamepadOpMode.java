package org.firstinspires.ftc.teampractice;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxResetMotorEncoderCommand;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name="Gamepad")
public class GamepadOpMode extends LinearOpMode {
    static volatile boolean dpad_down_pressed = false;
    static volatile boolean a_pressed = false;
    static volatile boolean b_pressed = false;

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot();
        robot.init(hardwareMap, false);

        LynxModule controlHub = hardwareMap.get(LynxModule.class, "Control Hub");

        int liftMotorPort = robot.armMotor.getPortNumber();

        robot.grabberServo.setPosition(0d);

        telemetry.addData(">", "Press Play to start op mode");
        telemetry.addData("target", String.format("tol: %d",
                robot.armMotor.getTargetPositionTolerance()));
        telemetry.addData("pidf",
                robot.armMotor.getPIDFCoefficients(DcMotorEx.RunMode.RUN_TO_POSITION).toString());
        telemetry.addData("over current",
                String.format("%.2f", robot.armMotor.getCurrentAlert(CurrentUnit.AMPS)));
        telemetry.addData("lift", () ->
                String.format("pos: %d, cur: %.2fA",
                        robot.armMotor.getCurrentPosition(),
                        robot.armMotor.getCurrent(CurrentUnit.AMPS)));
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.left_trigger > 0d) {
                robot.armMotor.setTargetPosition(1950);
                robot.setArmMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                robot.armMotor.setPower(gamepad1.left_trigger);
            } else if (gamepad1.right_trigger > 0d) {
                robot.armMotor.setTargetPosition(0);
                robot.setArmMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                robot.armMotor.setPower(gamepad1.right_trigger);
            } else {
                if (gamepad1.dpad_up) {
                    robot.setArmMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
                    robot.armMotor.setPower(0.3d);
                } else if (gamepad1.dpad_down) {
                    robot.setArmMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
                    robot.armMotor.setPower(-0.3d);
                } else {
                    robot.armMotor.setPower(0d);
                }
            }

            if (dpad_down_pressed && !gamepad1.dpad_down) {
                try {
                    new LynxResetMotorEncoderCommand(controlHub, liftMotorPort).send();
                } catch (LynxNackException e) {
                    e.printStackTrace();
                }
            }

            if (gamepad1.a) {
                robot.setGrabber(1d);
            } else if (gamepad1.b) {
                robot.setGrabber(0d);
            }

            double drive = Math.tan((-gamepad1.left_stick_y - gamepad1.right_stick_y) * Math.tan(1d));
            double turn = Math.atan(gamepad1.left_stick_x * Math.tan(0.6d));
            double side = Math.atan(gamepad1.right_stick_x * Math.tan(1d));

            double pLeftFront = Range.clip(drive + turn + side, -1.0d, 1.0d);
            double pLeftRear = Range.clip(drive + turn - side, -1.0d, 1.0d);
            double pRightFront = Range.clip(drive - turn - side, -1.0d, 1.0d);
            double pRightRear = Range.clip(drive - turn + side, -1.0d, 1.0d);

            // Send calculated power to wheels
            robot.setDrivePower(pLeftFront, pLeftRear, pRightFront, pRightRear);

            dpad_down_pressed = gamepad1.dpad_down;
            a_pressed = gamepad1.a;
            b_pressed = gamepad1.b;

            telemetry.update();
        }
    }
}
