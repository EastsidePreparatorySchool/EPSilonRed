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
    int[] northeast = new int[]{4, 0};
    int[] northwest = new int[]{0, 0};
    int[] southeast = new int[]{4, 4};
    int[] southwest = new int[]{0, 4};

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
        TimeUnit.MILLISECONDS.sleep(2000);

        //shoot both balls
        /*crossbow.fire();
        motorCollector.setPower(1);
        TimeUnit.SECONDS.sleep(5);
        motorCollector.setPower(0);
        crossbow.fire();
        TimeUnit.SECONDS.sleep(1);*/

        //knock the yoga ball off
        move(south, 1500);
        move(east, 750);
        move(southwest, 2500);
        move(north, 1000);
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
        move(west, 300);

        //align parallel to the wall
        motorRight1.setPower(rearRightMatrix[north[0]][north[1]] / 4);
        motorLeft1.setPower(frontLeftMatrix[north[0]][north[1]] / 4);
        motorRight2.setPower(frontRightMatrix[north[0]][north[1]] / 4);
        motorLeft2.setPower(rearLeftMatrix[north[0]][north[1]] / 4);
        while(!leftaligned && !rightaligned) {
            if(leftdist.getLightDetected() > 0.1) {
                motorRight1.setPower(0);
                motorRight2.setPower(0);
                leftaligned = true;
            }
            if(rightdist.getLightDetected() > 0.1) {
                motorLeft1.setPower(0);
                motorLeft2.setPower(0);
                rightaligned = true;
            }
        }
        rotator.gyro.calibrate();
        while(rotator.gyro.isCalibrating()) {}

        //move to beacon
        move(south, 50);
        move(east, 100);

        //find the right color
        int leftblue = leftcolor.blue();
        int rightblue = rightcolor.blue();
        if((leftblue - rightblue) > 3) {
            move(north, 100);
            move(east, 100);
            move(west, 100);
        }
        if((rightblue - leftblue) > 3) {
            move(south, 100);
            move(east, 100);
            move(west, 100);
        }
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
