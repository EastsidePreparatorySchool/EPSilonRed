package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

import java.util.concurrent.TimeUnit;

/**
 * Created by ninjas28 on 2016-10-28.
 */

public class EPSColorAuto extends OpMode {
    DcMotor motorRight1;
    DcMotor motorRight2;
    DcMotor motorLeft1;
    DcMotor motorLeft2;

    ColorSensor colour;
    OpticalDistanceSensor distSense;

    int precisionModeDrive = 0;
    int precisionModeArm = 0;

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

    int[] north = new int[]{0, 2};
    int[] west = new int[]{2, 0};
    int[] east = new int[]{2, 4};
    int[] south = new int[]{4, 2};
    int[] northeast = new int[]{0, 4};
    int[] northwest = new int[]{0, 0};
    int[] southeast = new int[]{4, 4};
    int[] southwest = new int[]{4, 0};

    @Override
    public void init() {
        colour = hardwareMap.colorSensor.get("color_sensor");
        distSense = hardwareMap.opticalDistanceSensor.get("dist_sensor");

        motorRight1 = hardwareMap.dcMotor.get("motor_1a");
        motorRight2 = hardwareMap.dcMotor.get("motor_1b");
        motorLeft1 = hardwareMap.dcMotor.get("motor_2b");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2a");
    }

    @Override
    public void start() {
        //IN HONOR OF HENRY MENG'S VALIANT HUMILIATION AND USAGE OF WHILE(TRUE)
        while(true) {
            motorLeft1.setDirection(DcMotor.Direction.FORWARD);
            motorRight1.setDirection(DcMotor.Direction.REVERSE);
            motorLeft2.setDirection(DcMotor.Direction.FORWARD);
            motorRight2.setDirection(DcMotor.Direction.REVERSE);
            break;
        }
    }

    @Override
    public void loop() {

        //move(northwest, (long)0.5);
        //TODO: MAKE COLOR CODE

    }

    private void move(int[] direction, long time) {
        for(long stop = System.nanoTime()+ TimeUnit.SECONDS.toNanos(time); stop>System.nanoTime();) {
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
