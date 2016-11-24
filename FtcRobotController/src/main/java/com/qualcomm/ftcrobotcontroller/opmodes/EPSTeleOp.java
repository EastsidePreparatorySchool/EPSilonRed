package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

import java.util.concurrent.TimeUnit;

/**
 * Created by supertrevor on 2015-11-13.
 */

public class EPSTeleOp extends OpMode {
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorRight2;
    DcMotor motorLeft2;
    DcMotor motorWinch;
    DcMotor motorCollector;
    Servo trigger;
    Servo cross;
    ServoController sc;

    int precisionModeDrive;
    int precisionModeArm;

    final int precisionDivider = 3; //This number is the divisor of the motor matrix value.

    final double[] yAxisMatrix = new double[]{-0.75, -0.30, 0.30, 0.75};
    final double[] xAxisMatrix = new double[]{-0.75, -0.30, 0.30, 0.75};

    //final double[] preciseXMatrix = new double[]{-0.9,-0.7,-0.5,-0.3,-0.1, 0.1, 0.3, 0.5, 0.7, 0.9};
    //final double[] collectorMatrix = new double[]{-1.0,-0.8,-0.6,-0.4,-0.2, 0.0, 0.2, 0.4, 0.6, 0.8, 1.0};
    //More array values allows for greater precision when controlling the motor with a joystick, used for the winch and collector

    final double[] rotationMatrix = new double[]{-1.0,-0.5, 0.0, 0.5, 1.0};

    //Hypothetically you could create an identity matrix on which you perform operations to get each respective motor value, but this is less confusing to understand.
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

    int cyPrev = 2;
    int wyPrev = 2;
    int rxPrev = 2;
    int ixPrev = 2;
    int iyPrev = 2;

    @Override
    public void init() {
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

        precisionModeDrive = 0;
        precisionModeArm = 0;
    }

    @Override
    public void start() {
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
    }

    @Override
    public void loop() {
        double joy1y1 = gamepad1.left_stick_y;
        double joy1x1 = gamepad1.left_stick_x;
        double joy1x2 = gamepad1.right_stick_x;
        double joy2y1 = gamepad2.left_stick_y;
        double joy2y2 = gamepad2.right_stick_y;

        //if(Math.abs(joy2y1) > 5) { //collector control
        int cy;
        for (cy = 0; cy < 4; cy++) {
            if (joy2y1 < xAxisMatrix[cy]) {
                break;
            }
        }
        if (cyPrev != cy) {
            motorCollector.setPower(rotationMatrix[cy]);
            cyPrev = cy;
        }
        //}

        //if(Math.abs(joy2y2) > 5) { //winch control
        int wy;
        for (wy = 0; wy < 4; wy++) {
            if (joy2y2 < xAxisMatrix[wy]) {
                break;
            }
        }
        if (wyPrev != wy) {
            motorWinch.setPower(rotationMatrix[wy]);
            wyPrev = wy;
        }
        //}

        //if (Math.abs(joy1x2) > 5) { //Rotate the robot
        int rx;
        for (rx = 0; rx < 4; rx++) {
            if (joy1x2 < xAxisMatrix[rx]) {
                break;
            }
        }
        if (precisionModeDrive == 1 && rxPrev != rx) {
            motorRight1.setPower((-1.0 * rotationMatrix[rx]) / precisionDivider);
            motorLeft1.setPower((rotationMatrix[rx]) / precisionDivider);
            motorRight2.setPower((-1.0 * rotationMatrix[rx]) / precisionDivider);
            motorLeft2.setPower((rotationMatrix[rx]) / precisionDivider);
            rxPrev = rx;
        } else if (rxPrev != rx){
            motorRight1.setPower(-1.0 * rotationMatrix[rx]);
            motorLeft1.setPower(rotationMatrix[rx]);
            motorRight2.setPower(-1.0 * rotationMatrix[rx]);
            motorLeft2.setPower(rotationMatrix[rx]);
            rxPrev = rx;
        }
        //}
        //else {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        int ix, iy;

        for (iy = 0; iy < 4; iy++) {
            if (joy1y1 < yAxisMatrix[iy]) {
                break;
            }
        }
        for (ix = 0; ix < 4; ix++) {
            if (joy1x1 < xAxisMatrix[ix]) {
                break;
            }
        }
        if(ixPrev != ix || iyPrev != iy) {
            if (precisionModeDrive == 1) {
                motorRight1.setPower((rearRightMatrix[ix][iy]) / precisionDivider);
                motorLeft1.setPower((frontLeftMatrix[ix][iy]) / precisionDivider);
                motorRight2.setPower((frontRightMatrix[ix][iy]) / precisionDivider);
                motorLeft2.setPower((rearLeftMatrix[ix][iy]) / precisionDivider);
            } else {
                motorRight1.setPower(rearRightMatrix[ix][iy]);
                motorLeft1.setPower(frontLeftMatrix[ix][iy]);
                motorRight2.setPower(frontRightMatrix[ix][iy]);
                motorLeft2.setPower(rearLeftMatrix[ix][iy]);
            }
            ixPrev = ix;
            iyPrev = iy;
        }


        if(gamepad1.a == true) { //Slows the motors
            precisionModeDrive = 1;
        }

        if(gamepad1.b == true) {
            precisionModeDrive = 0;
        }

        if(gamepad2.y == true) {
            try {
                fire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(gamepad2.left_bumper == true) { //Servo up
            trigger.setPosition(0.07);
        }

        if(gamepad2.right_bumper == true) { //Servo down
            trigger.setPosition(0.3);
        }

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", joy1y1));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", joy1x1));
        telemetry.addData("trigger pos", "trigger: " + String.format("%.2f", trigger.getPosition()));
    }

    public void fire() throws InterruptedException {
        motorWinch.setPower(1.0);
        TimeUnit.SECONDS.sleep(2);
        motorWinch.setPower(0.0);
        trigger.setPosition(0.3);
        motorWinch.setPower(-0.4);
        TimeUnit.SECONDS.sleep(2);
        motorWinch.setPower(0.0);
        trigger.setPosition(0.07);
    }

    private void stopDriving() {
        motorRight1.setPower(0);
        motorLeft1.setPower(0);
        motorRight2.setPower(0);
        motorLeft2.setPower(0);
    }
    /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
    //UNUSED, CAN DELETE.
    /* double scaleInput(double dVal)  {
        double[] scaleArray = { 0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24,
                0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00 };


        // get the corresponding index for the scaleInput array.
        int index = (int) (dVal * 16.0);

        // index should be positive.
        if (index < 0) {
            index = -index;
        }

        // index cannot exceed size of array minus 1.
        if (index > 16) {
            index = 16;
        }

        // get value from the array.
        double dScale = 0.0;
        if (dVal < 0) {
            dScale = -scaleArray[index];
        } else {
            dScale = scaleArray[index];
        }

        // return scaled value.
        return dScale;
    } */
}