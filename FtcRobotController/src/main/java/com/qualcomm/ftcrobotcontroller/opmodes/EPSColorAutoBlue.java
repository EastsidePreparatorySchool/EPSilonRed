package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.TouchSensor;

import java.util.concurrent.TimeUnit;

/**
 * Created by ninjas28 on 2016-10-28.
 */

public class EPSColorAutoBlue extends LinearOpMode {
    DcMotor motorRight1;
    DcMotor motorRight2;
    DcMotor motorLeft1;
    DcMotor motorLeft2;
    DcMotor motorWinch;
    DcMotor motorCollector;
    Servo trigger;
    ServoController sc;

    ColorSensor colour;
    OpticalDistanceSensor distSense;
    GyroSensor gyro;

    AutoRotator rotator;
    TouchSensor touch;

    int precisionModeDrive = 0;
    int precisionModeArm = 0;

    int shootingHeading = 0;

    final int precisionDivider = 3;

    boolean foundBlue = false;

    final double[] yAxisMatrix = new double[]{-0.75, -0.30, 0.30, 0.75};
    final double[] xAxisMatrix = new double[]{-0.75, -0.30, 0.30, 0.75};

    final double[] rotationMatrix = new double[]{-1.0, -0.5, 0.0, 0.5, 1.0};

    final double[][] frontLeftMatrix = new double[][]{
            {0.0, 0.0, -1.0, -1.0, -1.0},
            {0.0, 0.0, -0.5, -1.0, -1.0},
            {1.0, 0.5, 0.0, -0.5, -1.0},
            {1.0, 1.0, 0.5, 0.0, 0.0},
            {1.0, 1.0, 1.0, 0.0, 0.0}
    };
    final double[][] frontRightMatrix = new double[][]{
            {1.0, 1.0, 1.0, 0.0, 0.0},
            {1.0, 1.0, 0.5, 0.0, 0.0},
            {1.0, 0.5, 0.0, -0.5, -1.0},
            {0.0, 0.0, -0.5, -1.0, -1.0},
            {0.0, 0.0, -1.0, -1.0, -1.0}
    };
    final double[][] rearLeftMatrix = new double[][]{
            {1.0, 1.0, 1.0, 0.0, 0.0},
            {1.0, 1.0, 0.5, 0.0, 0.0},
            {1.0, 0.5, 0.0, -0.5, -1.0},
            {0.0, 0.0, -0.5, -1.0, -1.0},
            {0.0, 0.0, -1.0, -1.0, -1.0}
    };
    final double[][] rearRightMatrix = new double[][]{
            {0.0, 0.0, -1.0, -1.0, -1.0},
            {0.0, 0.0, -0.5, -1.0, -1.0},
            {1.0, 0.5,  0.0, -0.5, -1.0},
            {1.0, 1.0,  0.5,  0.0,  0.0},
            {1.0, 1.0,  1.0,  0.0,  0.0}
    };

    boolean hitBlack = false;
    int[] north = new int[]{2, 0};
    int[] west = new int[]{0, 2};
    int[] east = new int[]{4, 2};
    int[] south = new int[]{2, 4};
    int[] northeast = new int[]{4, 0};
    int[] northwest = new int[]{0, 0};
    int[] southeast = new int[]{4, 4};
    int[] southwest = new int[]{0, 4};

    @Override
    public void runOpMode() throws InterruptedException {
        colour = hardwareMap.colorSensor.get("color");
        distSense = hardwareMap.opticalDistanceSensor.get("dist");
        gyro = hardwareMap.gyroSensor.get("gyro");
        touch = hardwareMap.touchSensor.get("touch");
        motorCollector = hardwareMap.dcMotor.get("motor_coll");
        motorRight1 = hardwareMap.dcMotor.get("motor_1a");
        motorRight2 = hardwareMap.dcMotor.get("motor_1b");
        motorLeft1 = hardwareMap.dcMotor.get("motor_2b");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2a");
        motorWinch = hardwareMap.dcMotor.get("motor_win");
        trigger = hardwareMap.servo.get("trigger");

        sc = hardwareMap.servoController.get("Servo Controller 1");
        sc.pwmEnable();

        //IN HONOR OF HENRY MENG'S VALIANT HUMILIATION AND USAGE OF WHILE(TRUE)
        while (true) {
            motorLeft1.setDirection(DcMotor.Direction.FORWARD);
            motorRight1.setDirection(DcMotor.Direction.REVERSE);
            motorLeft2.setDirection(DcMotor.Direction.FORWARD);
            motorRight2.setDirection(DcMotor.Direction.REVERSE);
            break;
        }

        Catapult crossbow = new Catapult(motorWinch, trigger);
        rotator = new AutoRotator(gyro, motorLeft1, motorLeft2, motorRight1, motorRight2);
        rotator.init();

        waitForStart();
        rotator.gyro.resetZAxisIntegrator();

        //shoot both balls
        /*crossbow.fire();
        motorCollector.setPower(1);
        TimeUnit.SECONDS.sleep(5);
        motorCollector.setPower(0);
        crossbow.fire();
        TimeUnit.SECONDS.sleep(1);*/

        //knock the yoga ball off
        move(south, 1000);
        move(east, 500);
        move(southwest, 2500);
        move(north, 900);
        rotator.rotate(180);
        TimeUnit.MILLISECONDS.sleep(500);

        //move to the wall
        motorRight1.setPower(rearRightMatrix[east[0]][east[1]] / 3);
        motorLeft1.setPower(frontLeftMatrix[east[0]][east[1]] / 3);
        motorRight2.setPower(frontRightMatrix[east[0]][east[1]] / 3);
        motorLeft2.setPower(rearLeftMatrix[east[0]][east[1]] / 3);
        while(!touch.isPressed()) {}
        stopDriving();
        TimeUnit.MILLISECONDS.sleep(100);
        move(west, 250);

        //align parallel to the wall
        int heading = rotator.gyro.getHeading();
        if(heading <= 177 && heading > 0) {
            rotator.manuallyRotate(0, "right", 0.13);
        } else if (heading >= 183 && heading <= 359) {
            rotator.manuallyRotate(0, "left", 0.13);
        }

        motorRight1.setPower(rearRightMatrix[north[0]][north[1]] / 4);
        motorLeft1.setPower(frontLeftMatrix[north[0]][north[1]] / 4);
        motorRight2.setPower(frontRightMatrix[north[0]][north[1]] / 4);
        motorLeft2.setPower(rearLeftMatrix[north[0]][north[1]] / 4);

        //find beacon
        while(colour.blue() < 3) {}
        if(colour.blue() > 3) {
            foundBlue = true;
        }
        while(foundBlue) {
            if (colour.blue() < 1) {
                stopDriving();
                break;
            }
        }
        //hit beacon
        move(east, 200);
        TimeUnit.MILLISECONDS.sleep(50);
        rotator.manuallyRotate(175, "left", 0.15);
        TimeUnit.MILLISECONDS.sleep(100);
        rotator.manuallyRotate(180, "right", 0.15);

    }

    private void move(int[] direction, long time) {
        for(long stop = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(time); stop > System.nanoTime();) {
            motorRight1.setPower(rearRightMatrix[direction[0]][direction[1]]);
            motorLeft1.setPower(frontLeftMatrix[direction[0]][direction[1]]);
            motorRight2.setPower(frontRightMatrix[direction[0]][direction[1]]);
            motorLeft2.setPower(rearLeftMatrix[direction[0]][direction[1]]);
        }
        stopDriving();

    }
    private void stopDriving() {
        motorRight1.setPower(rearRightMatrix[2][2]);
        motorLeft1.setPower(frontLeftMatrix[2][2]);
        motorRight2.setPower(frontRightMatrix[2][2]);
        motorLeft2.setPower(rearLeftMatrix[2][2]);
    }
}
