package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
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
public class EPSMasterAutonomous extends LinearOpMode {
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorRight2;
    DcMotor motorLeft2;
    DcMotor motorWinch;
    DcMotor motorCollector;
    Servo trigger;
    ServoController sc;

    ColorSensor leftcolor;
    OpticalDistanceSensor leftdist;
    ColorSensor rightcolor;
    OpticalDistanceSensor rightdist;
    TouchSensor touch;
    GyroSensor gyro;

    AutoDriver driveTrain;
    AutoRotator rotator;
    Catapult crossbow;

    int[] north = new int[]{2, 0};
    int[] west = new int[]{0, 2};
    int[] east = new int[]{4, 2};
    int[] south = new int[]{2, 4};
    int[] northeast = new int[]{4, 0};
    int[] northwest = new int[]{0, 0};
    int[] southeast = new int[]{4, 4};
    int[] southwest = new int[]{0, 4};

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
        while(true) {
            motorLeft1.setDirection(DcMotor.Direction.FORWARD);
            motorRight1.setDirection(DcMotor.Direction.REVERSE);
            motorLeft2.setDirection(DcMotor.Direction.FORWARD);
            motorRight2.setDirection(DcMotor.Direction.REVERSE);
            motorCollector.setDirection(DcMotor.Direction.REVERSE);
            motorWinch.setDirection(DcMotor.Direction.FORWARD);
            break;
        }

        waitForStart();

    }
}
