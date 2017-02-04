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

import java.util.concurrent.TimeUnit;

/**
 * Created by ninjas28 on 2016-10-28.
 */
@Autonomous(name = "BlueEverything", group = "All")
public class EPSColorAutoBlue extends LinearOpMode {
    DcMotor motorRight1;
    DcMotor motorRight2;
    DcMotor motorLeft1;
    DcMotor motorLeft2;
    DcMotor motorWinch;
    DcMotor motorCollector;
    Servo trigger;
    ServoController sc;

    ColorSensor leftcolor;
    OpticalDistanceSensor leftdist;
    ColorSensor rightcolor;
    OpticalDistanceSensor rightdist;
    GyroSensor gyro;

    AutoRotator rotator;
    TouchSensor touch;

    final int precisionDivider = 3;

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

    int[] north = new int[]{2, 0};
    int[] west = new int[]{0, 2};
    int[] east = new int[]{4, 2};
    int[] south = new int[]{2, 4};
    int[] northeast = new int[]{0, 4};
    int[] northwest = new int[]{0, 0};
    int[] southeast = new int[]{4, 4};
    int[] southwest = new int[]{4, 0};

    boolean leftaligned = false;
    boolean rightaligned = false;
    boolean aligned = false;

    @Override
    public void runOpMode() throws InterruptedException {
        leftcolor = hardwareMap.colorSensor.get("leftcolor");
        leftdist = hardwareMap.opticalDistanceSensor.get("leftdist");
        rightcolor = hardwareMap.colorSensor.get("rightcolor");
        rightdist = hardwareMap.opticalDistanceSensor.get("rightdist");
        gyro = hardwareMap.gyroSensor.get("gyro");
        touch = hardwareMap.touchSensor.get("touch");
        motorCollector = hardwareMap.dcMotor.get("motor_coll");
        motorRight1 = hardwareMap.dcMotor.get("motor_1a");
        motorRight2 = hardwareMap.dcMotor.get("motor_1b");
        motorLeft1 = hardwareMap.dcMotor.get("motor_2b");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2a");
        motorWinch = hardwareMap.dcMotor.get("motor_win");
        trigger = hardwareMap.servo.get("trigger");

        leftcolor.setI2cAddress(I2cAddr.create8bit(0x3c));
        rightcolor.setI2cAddress(I2cAddr.create8bit(0x4c));

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
        sleep(500);

        //shoot both balls
        crossbow.fire();
        /*motorCollector.setPower(1);
        TimeUnit.MILLISECONDS.sleep(3000);
        motorCollector.setPower(0);
        crossbow.fire();*/
        sleep(1000);

        //knock the yoga ball off
        /*move(south, 1300);
        move(east, 700);
        move(southwest, 2500);
        move(north, 1000);*/
        move(northeast, 3200);
        rotator.manuallyRotate(170, "left", 0.15);
        sleep(500);

        //move to the wall
        motorRight1.setPower(rearRightMatrix[east[0]][east[1]] / 3);
        motorLeft1.setPower(frontLeftMatrix[east[0]][east[1]] / 3);
        motorRight2.setPower(frontRightMatrix[east[0]][east[1]] / 3);
        motorLeft2.setPower(rearLeftMatrix[east[0]][east[1]] / 3);
        while(!touch.isPressed()) {}
        stopDriving();
        sleep(100);
        move(west, 300);
        //rotator.rotate(170);

        //align parallel to the wall
        motorRight1.setPower((rearRightMatrix[north[0]][north[1]]) / 7);
        motorLeft1.setPower((frontLeftMatrix[north[0]][north[1]]) / 8);
        motorRight2.setPower((frontRightMatrix[north[0]][north[1]]) / 7);
        motorLeft2.setPower((rearLeftMatrix[north[0]][north[1]]) / 8);
        while(!leftaligned && !rightaligned) {
            if(rightdist.getLightDetected() > 0.08) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                leftaligned = true;
            }
            if(leftdist.getLightDetected() > 0.08) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                rightaligned = true;
            }
            waitOneFullHardwareCycle();
        }
        rotator.manuallyRotate(170, "right", 0.15);
        sleep(1000);
        //rotator.gyro.calibrate();
        //while(rotator.gyro.isCalibrating()) {}

        //move to beacon
        /*move(south, 250);
        move(east, 150);/
        */
        //find the right color
        boolean thingthatisrequiredtoworkproperly = false;

        int leftblue = leftcolor.blue();
        int rightblue = rightcolor.blue();
        if(rightblue >= 3) {
            move(east, 500, 3);
            move(west, 500, 3);
            thingthatisrequiredtoworkproperly = true;
        } else {
            motorRight1.setPower((rearRightMatrix[south[0]][south[1]]) / 6);
            motorLeft1.setPower((frontLeftMatrix[south[0]][south[1]]) / 6);
            motorRight2.setPower((frontRightMatrix[south[0]][south[1]]) / 6);
            motorLeft2.setPower((rearLeftMatrix[south[0]][south[1]]) / 6);
            while(leftblue < 3) {
                leftblue = leftcolor.blue();
            }
            stopDriving();
            move(north, 50, 3);
            move(east, 500, 3);
            move(west, 500, 3);
            rightaligned = false;
            leftaligned = false;
        }
        if(thingthatisrequiredtoworkproperly) {
            motorRight1.setPower((rearRightMatrix[north[0]][north[1]]) / 8);
            motorLeft1.setPower((frontLeftMatrix[north[0]][north[1]]) / 8);
            motorRight2.setPower((frontRightMatrix[north[0]][north[1]]) / 8);
            motorLeft2.setPower((rearLeftMatrix[north[0]][north[1]]) / 8);
            while (!leftaligned && !rightaligned) {
                if (rightdist.getLightDetected() > 0.1) {
                    motorRight1.setPower(0);
                    motorRight2.setPower(0);
                    leftaligned = true;
                }
                if (leftdist.getLightDetected() > 0.1) {
                    motorLeft1.setPower(0);
                    motorLeft2.setPower(0);
                    rightaligned = true;
                }
            }
        }
        for(long stop = System.nanoTime() + TimeUnit.MILLISECONDS.toNanos(1500); stop > System.nanoTime();) {
            motorRight1.setPower(rearRightMatrix[north[0]][north[1]] / 3);
            motorLeft1.setPower(frontLeftMatrix[north[0]][north[1]] / 5);
            motorRight2.setPower(frontRightMatrix[north[0]][north[1]] / 3);
            motorLeft2.setPower(rearLeftMatrix[north[0]][north[1]] / 5);
        }
        stopDriving();
        leftaligned = false;
        rightaligned = false;
        motorRight1.setPower((rearRightMatrix[north[0]][north[1]]) / 8);
        motorLeft1.setPower((frontLeftMatrix[north[0]][north[1]]) / 8);
        motorRight2.setPower((frontRightMatrix[north[0]][north[1]]) / 8);
        motorLeft2.setPower((rearLeftMatrix[north[0]][north[1]]) / 8);
        while(!leftaligned && !rightaligned) {
            if(rightdist.getLightDetected() > 0.08) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                leftaligned = true;
            }
            if(leftdist.getLightDetected() > 0.08) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                rightaligned = true;
            }
        }
        move(north, 500, 2);
        motorRight1.setPower(rearRightMatrix[east[0]][east[1]] / 3);
        motorLeft1.setPower(frontLeftMatrix[east[0]][east[1]] / 3);
        motorRight2.setPower(frontRightMatrix[east[0]][east[1]] / 3);
        motorLeft2.setPower(rearLeftMatrix[east[0]][east[1]] / 3);
        while(!touch.isPressed()) {}
        stopDriving();
        sleep(100);
        move(west, 300);
        move(south, 400, 2);
        leftblue = leftcolor.blue();
        rightblue = rightcolor.blue();
        if(rightblue >= 3) {
            move(east, 500, 3);
            move(west, 500, 3);
        } else {
            motorRight1.setPower((rearRightMatrix[south[0]][south[1]]) / 6);
            motorLeft1.setPower((frontLeftMatrix[south[0]][south[1]]) / 6);
            motorRight2.setPower((frontRightMatrix[south[0]][south[1]]) / 6);
            motorLeft2.setPower((rearLeftMatrix[south[0]][south[1]]) / 6);
            while(leftblue < 3) {
                leftblue = leftcolor.blue();
            }
            stopDriving();
            move(north, 50, 3);
            move(east, 500, 3);
            move(west, 500, 3);
        }
    }

    private void move(int[] direction, long time) {
        motorRight1.setPower(rearRightMatrix[direction[0]][direction[1]]);
        motorLeft1.setPower(frontLeftMatrix[direction[0]][direction[1]]);
        motorRight2.setPower(frontRightMatrix[direction[0]][direction[1]]);
        motorLeft2.setPower(rearLeftMatrix[direction[0]][direction[1]]);
        sleep(time);
        stopDriving();

    }

    private void move(int[] direction, long time, int modifier) {
        motorRight1.setPower(rearRightMatrix[direction[0]][direction[1]] / modifier);
        motorLeft1.setPower(frontLeftMatrix[direction[0]][direction[1]] / modifier);
        motorRight2.setPower(frontRightMatrix[direction[0]][direction[1]] / modifier);
        motorLeft2.setPower(rearLeftMatrix[direction[0]][direction[1]] / modifier);
        sleep(time);
        stopDriving();

    }
    private void stopDriving() {
        motorRight1.setPower(rearRightMatrix[2][2]);
        motorLeft1.setPower(frontLeftMatrix[2][2]);
        motorRight2.setPower(frontRightMatrix[2][2]);
        motorLeft2.setPower(rearLeftMatrix[2][2]);
    }
}
