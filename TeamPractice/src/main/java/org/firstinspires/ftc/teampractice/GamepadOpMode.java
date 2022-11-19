package org.firstinspires.ftc.teampractice;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.lynx.LynxNackException;
import com.qualcomm.hardware.lynx.commands.core.LynxResetMotorEncoderCommand;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp(name = "Gamepad")
public class GamepadOpMode extends LinearOpMode {
    static volatile boolean back_pressed, left_bumper_pressed = false, right_bumper_pressed = false;
    static volatile int cp;

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot();
        robot.init(hardwareMap, false);
        int[] armStops = new int[]{0, 950, 1600, 2280};
        int i = 0;
        boolean autoArm = false;

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
        telemetry.addData("power", () ->
                String.format("%.2f", robot.armMotor.getPower()));
        telemetry.addData("limit",
                String.format("%b", robot.limitSwitch.getState()));
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            if (gamepad1.left_trigger > 0d) {
                robot.setArmTargetPosition(2450);
                robot.setArmMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                robot.setArmPower(gamepad1.left_trigger);
                cp = robot.armMotor.getCurrentPosition();
            } else if (gamepad1.right_trigger > 0d && robot.limitSwitch.getState() == true) {
                robot.setArmMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
                robot.setArmPower(-gamepad1.right_trigger * (cp < 700 ? 0.4d: 1d));
                cp = robot.armMotor.getCurrentPosition();
                autoArm = false;
            } else {
                if (autoArm == false && robot.limitSwitch.getState() == false) {
                    cp = 0;
                    robot.setArmMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                } else {
                    robot.setArmTargetPosition(cp);
                    robot.setArmMode(DcMotorEx.RunMode.RUN_TO_POSITION);
                    robot.setArmPower(0.8d);
                }
            }

            if (gamepad1.left_bumper && !left_bumper_pressed) {
                if (i < 3) {
                    i++;
                    cp = armStops[i];
                    autoArm = true;
                }
            }

            if (gamepad1.right_bumper && !right_bumper_pressed) {
                i = 0;
                cp = -100;
                autoArm = false;
            }

            if (gamepad1.a) {
                robot.setGrabber(0d);
            } else if (gamepad1.b) {
                robot.setGrabber(1d);
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

            left_bumper_pressed = gamepad1.left_bumper;
            right_bumper_pressed = gamepad1.right_bumper;

            telemetry.update();
        }
    }
}
