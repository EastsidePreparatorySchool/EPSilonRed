package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.concurrent.TimeUnit;

/**
 * Created by ninjas28 on 10/7/2016.
 */

public class EPSShooterAutonomous2RedMove extends LinearOpMode {
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorRight2;
    DcMotor motorLeft2;
    DcMotor motorWinch;
    DcMotor motorCollector;
    Servo trigger;
    Servo cross;
    ServoController sc;

    int precisionModeDrive = 0;
    int precisionModeArm = 0;

    final int precisionDivider = 3;

    final double[] yAxisMatrix = new double[]{-0.75, -0.30, 0.30, 0.75};
    final double[] xAxisMatrix = new double[]{-0.75, -0.30, 0.30, 0.75};

    final double[] rotationMatrix = new double[]{-1.0,-0.5, 0.0, 0.5, 1.0};

    final double[][] frontLeftMatrix = new double[][]{
            { 0.0, 0.0,-1.0,-1.0,-1.0},
            { 0.0, 0.0,-0.5,-1.0,-1.0},
            { 1.0, 0.5, 0.0,-0.5,-1.0},
            { 1.0, 1.0, 0.5, 0.0, 0.0},
            { 1.0, 1.0, 1.0, 0.0, 0.0}
    };
    final double[][] frontRightMatrix = new double[][]{
            { 1.0, 1.0, 1.0, 0.0, 0.0},
            { 1.0, 1.0, 0.5, 0.0, 0.0},
            { 1.0, 0.5, 0.0,-0.5,-1.0},
            { 0.0, 0.0,-0.5,-1.0,-1.0},
            { 0.0, 0.0,-1.0,-1.0,-1.0}
    };
    final double[][] rearLeftMatrix = new double[][]{
            { 1.0, 1.0, 1.0, 0.0, 0.0},
            { 1.0, 1.0, 0.5, 0.0, 0.0},
            { 1.0, 0.5, 0.0,-0.5,-1.0},
            { 0.0, 0.0,-0.5,-1.0,-1.0},
            { 0.0, 0.0,-1.0,-1.0,-1.0}
    };
    final double[][] rearRightMatrix = new double[][]{
            { 0.0, 0.0,-1.0,-1.0,-1.0},
            { 0.0, 0.0,-0.5,-1.0,-1.0},
            { 1.0, 0.5, 0.0,-0.5,-1.0},
            { 1.0, 1.0, 0.5, 0.0, 0.0},
            { 1.0, 1.0, 1.0, 0.0, 0.0}
    };

    int[] forward = new int[] {2,0};
    int[] left = new int[] {0,2};
    int[] right = new int[] {4,2};
    int[] backward = new int[] {2,4};

    @Override
    public void runOpMode() throws InterruptedException {
        motorRight1 = hardwareMap.dcMotor.get("motor_1a");
        motorRight2 = hardwareMap.dcMotor.get("motor_1b");
        motorLeft1 = hardwareMap.dcMotor.get("motor_2b");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2a");
        motorCollector = hardwareMap.dcMotor.get("motor_coll");
        motorWinch = hardwareMap.dcMotor.get("motor_win");
        trigger = hardwareMap.servo.get("trigger");
        //cross = hardwareMap.servo.get("cross");
        sc = hardwareMap.servoController.get("Servo Controller 1");
        sc.pwmEnable();

        Catapult crossbow = new Catapult(motorWinch, trigger);

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

        crossbow.fire();
        motorCollector.setPower(-1);
        TimeUnit.SECONDS.sleep(6);
        motorCollector.setPower(0);
        crossbow.fire();
        TimeUnit.SECONDS.sleep(1);
        move(left, 500);
        move(backward, 2800);
        move(left, 500);
    }

    public void move(int[] direction, long time) {
        for(long stop = System.nanoTime()+ TimeUnit.MILLISECONDS.toNanos(time); stop>System.nanoTime();) {
            motorRight1.setPower(rearRightMatrix[direction[0]][direction[1]]);
            motorLeft1.setPower(frontLeftMatrix[direction[0]][direction[1]]);
            motorRight2.setPower(frontRightMatrix[direction[0]][direction[1]]);
            motorLeft2.setPower(rearLeftMatrix[direction[0]][direction[1]]);
        }
        stopDriving();

    }

    public void stopDriving() {
        motorRight1.setPower(rearRightMatrix[2][2]);
        motorLeft1.setPower(frontLeftMatrix[2][2]);
        motorRight2.setPower(frontRightMatrix[2][2]);
        motorLeft2.setPower(rearLeftMatrix[2][2]);
    }
}
