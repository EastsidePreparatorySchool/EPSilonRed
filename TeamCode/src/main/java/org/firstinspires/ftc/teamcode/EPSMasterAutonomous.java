package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;

/**
 * Created by ninjas28 on 2017-01-20.
 */

@Autonomous(name = "EPSAutonomous", group = "EPSilon Red (8103)")
@Disabled
public class EPSMasterAutonomous extends LinearOpMode {
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorRight2;
    DcMotor motorLeft2;
    DcMotor motorWinch;
    DcMotor motorCollector;
    Servo trigger;
    ServoController sc;

    private ColorSensor leftcolor;
    private OpticalDistanceSensor leftdist;
    private ColorSensor rightcolor;
    private OpticalDistanceSensor rightdist;
    private TouchSensor touch;
    private GyroSensor gyro;

    private AutoDriver driveTrain;
    private AutoRotator rotator;
    private Catapult crossbow;

    int[] north = new int[]{2, 0};
    int[] west = new int[]{0, 2};
    int[] east = new int[]{4, 2};
    int[] south = new int[]{2, 4};
    int[] northeast = new int[]{4, 0};
    int[] northwest = new int[]{0, 0};
    int[] southeast = new int[]{4, 4};
    int[] southwest = new int[]{0, 4};

    private String allianceColor = "";
    private boolean capBallKnockOff = false;
    private boolean beaconPushing = false;
    private boolean shootBalls = false;
    private int beacons = 0;
    private int balls = 0;


    @Override
    public void runOpMode () {
        motorRight1 = hardwareMap.dcMotor.get("motor_1a");
        motorRight2 = hardwareMap.dcMotor.get("motor_1b");
        motorLeft1 = hardwareMap.dcMotor.get("motor_2b");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2a");
        motorCollector = hardwareMap.dcMotor.get("motor_coll");
        motorWinch = hardwareMap.dcMotor.get("motor_win");
        trigger = hardwareMap.servo.get("trigger");
        sc = hardwareMap.servoController.get("Servo Controller 1");
        sc.pwmEnable();

        leftcolor = hardwareMap.colorSensor.get("leftcolor");
        leftdist = hardwareMap.opticalDistanceSensor.get("leftdist");
        rightcolor = hardwareMap.colorSensor.get("rightcolor");
        rightdist = hardwareMap.opticalDistanceSensor.get("rightdist");
        gyro = hardwareMap.gyroSensor.get("gyro");
        touch = hardwareMap.touchSensor.get("touch");

        leftcolor.setI2cAddress(I2cAddr.create8bit(0x3c));
        rightcolor.setI2cAddress(I2cAddr.create8bit(0x4c));

        crossbow = new Catapult(motorWinch, trigger);
        driveTrain = new AutoDriver(motorRight1, motorLeft1, motorRight2, motorLeft2);
        rotator = new AutoRotator(gyro, motorLeft1, motorLeft2, motorRight1, motorRight2);
        rotator.init();

        //IN HONOR OF HENRY MENG'S VALIANT HUMILIATION AND USAGE OF WHILE(TRUE)
        //aka an infinite loop that breaks after one loop
        while(true) {
            motorLeft1.setDirection(DcMotor.Direction.FORWARD);
            motorRight1.setDirection(DcMotor.Direction.REVERSE);
            motorLeft2.setDirection(DcMotor.Direction.FORWARD);
            motorRight2.setDirection(DcMotor.Direction.REVERSE);
            motorCollector.setDirection(DcMotor.Direction.REVERSE);
            motorWinch.setDirection(DcMotor.Direction.FORWARD);
            break;
        }

        telemetry.addData("1","Alliance Color:" + allianceColor);
        telemetry.addData("2","Cap Ball Knockoff:", Boolean.toString(capBallKnockOff));
        telemetry.addLine();
        telemetry.addData("3","Beacon Pushing:", Boolean.toString(beaconPushing));
        telemetry.addData("4","Number of Beacons:", Integer.toString(beacons));
        telemetry.addLine();
        telemetry.addData("5","Ball Shooting:", Boolean.toString(shootBalls));
        telemetry.addData("6","Number of Balls:", Integer.toString(balls));

        while(!gamepad1.start) {
            //choose blue alliance
            if(gamepad1.x) {
                allianceColor = "blue";
                telemetry.update();
            }
            //choose red alliance
            if(gamepad1.b) {
                allianceColor = "red";
                telemetry.update();
            }
            //Knock the cap ball off the center platform
            if(gamepad1.dpad_up) {
                capBallKnockOff = true;
                telemetry.update();
            }

            //Shoot the balls
            while(gamepad1.y) {
                if(gamepad1.left_bumper) {
                    balls--;
                }
                if(gamepad1.right_bumper) {
                    balls++;
                }
                telemetry.update();
            }
            //Push the beacons
            while(gamepad1.a) {
                if(gamepad1.left_bumper) {
                    beacons--;
                }
                if(gamepad1.right_bumper) {
                    beacons++;
                }
                telemetry.update();
            }
        }

        balls = Math.abs(balls);
        if(balls > 0) {
            shootBalls = true;
            if(balls > 3) {
                balls = 2;
            }
        }

        beacons = Math.abs(beacons);
        if(beacons > 0) {
            beaconPushing = true;
            if(beacons > 3) {
                beacons = 2;
            }
        }

        telemetry.update();
        waitForStart();

    }
}
