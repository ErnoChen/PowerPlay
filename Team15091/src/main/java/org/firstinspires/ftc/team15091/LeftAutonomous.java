package org.firstinspires.ftc.team15091;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.team15091.examples.HSVPipeline;

@com.qualcomm.robotcore.eventloop.opmode.Autonomous(name = "left", group = "Example", preselectTeleOp="Gamepad")
public class LeftAutonomous extends AutonomousBase{

    HSVPipeline pipeline;
    DcMotor armMotor;
    Servo grabberServo;
    int stored_colour;
    int highPolePos = 3000, mediumPolePos = 2000, lowPolePos = 1000, junctionPos = 500;
    Thread armUp = new Thread() {
        public void run() {
            armMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            armMotor.setTargetPosition(highPolePos);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(1);
        }
    };
    Thread armDown = new Thread() {
        public void run() {
            armMotor.setTargetPosition(0);
            armMotor.setPower(1);
            grabberServo.setPosition(0);
        }
    };
    Thread armToCone5 = new Thread() {
        public void run() {
            armMotor.setTargetPosition(cone5Pos);
            armMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            armMotor.setPower(1);
        }
    };
    Thread armToLowPole = new Thread() {
        public void run() {
            armMotor.setTargetPosition(lowPolePos);
            armMotor.setPower(1);
        }
    };
    ElapsedTime runtime = new ElapsedTime();
    @Override
    public void runOpMode() throws InterruptedException {
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        grabberServo = hardwareMap.get(Servo.class, "grabber_servo");
        pipeline = new HSVPipeline();
        pipeline.setupWebcam(hardwareMap);

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Analysis", () -> String.format("%d", pipeline.colour));
        telemetry.addData("Angle", () -> String.format("%.1f", robot.getHeading()));

        // Wait for the game to start (driver presses PLAY)
        // Abort this loop is started or stopped.
        setupAndWait();
        stored_colour = pipeline.colour;
        runtime.reset();
        armToCone5.start();
        robotDriver.gyroSlide(1, 37, 0, 5, null);
        robotDriver.gyroDrive(1, 75, 0, 3, null);
        sleep(500);
        armUp.start();
        robotDriver.gyroSlide(1, -37, 0, 5, null);
        robotDriver.gyroTurn(1, -46, 3);
        robotDriver.gyroDrive(1, 15, -46, 3, null);
        armDown.start();
        sleep(200);
        grabberServo.setPosition(0d);
        sleep(400);
        armToCone5.start();
        robotDriver.gyroDrive(1, -15, -46, 3, null);
        robotDriver.gyroTurn(1, 90, 3);
        robotDriver.gyroDrive(0.5, 35, 90, 3, null);
        grabberServo.setPosition(1d);
        sleep(500);
        armToLowPole.start();
        sleep(100);
        robotDriver.gyroTurn(1, 225, 4);
        robotDriver.gyroDrive(0.5, 4, 225, 2, null);
        grabberServo.setPosition(0d);
    }
}
