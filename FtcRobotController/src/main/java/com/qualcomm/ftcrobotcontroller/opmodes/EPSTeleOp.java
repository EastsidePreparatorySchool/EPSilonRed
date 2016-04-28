package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by supertrevor on 2015-11-13.
 */

public class EPSTeleOp extends OpMode {
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorRight2;
    DcMotor motorLeft2;

    int precisionModeDrive;
    int precisionModeArm;

    final double[] yAxisMatrix = new double[]{-0.75, -0.40, 0, 0.40, 0.75};
    final double[] xAxisMatrix = new double[]{-0.75, -0.40, 0, 0.40, 0.75};

    final double[][] frontLeftMatrix = new double[][]{
            { 0, 0, 1, 0.5, 1 },
            { 0, 0, 0.5, 0.5, 0.5 },
            { -1, -0.5, 0, 0.5, 1 },
            { -0.5, -0.5, -0.5, 0, 0 },
            { -1, -0.5, -1, 0, 0 }
    };
    final double[][] frontRightMatrix = new double[][]{
            { 1, 0.5, 1, 0, 0 },
            { 0.5, 0.5, 0.5, 0, 0 },
            { 1, 0.5, 0, -0.5, -1 },
            { 0, 0, -0.5, -0.5, -0.5 },
            { 0, 0, -1, -0.5, -1 }
    };
    final double[][] rearLeftMatrix = new double[][]{
            { 1, 0.5, 1, 0, 0 },
            { 0.5, 0.5, 0.5, 0, 0 },
            { 1, 0.5, 0, -0.5, -1 },
            { 0, 0, -0.5, -0.5, -0.5 },
            { 0, 0, -1, -0.5, -1 }
    };
    final double[][] rearRightMatrix = new double[][]{
            { 0, 0, 1, 0.5, 1 },
            { 0, 0, 0.5, 0.5, 0.5 },
            { -1, -0.5, 0, 0.5, 1 },
            { -0.5, -0.5, -0.5, 0, 0 },
            { -1, -0.5, -1, 0, 0 }
    };

    @Override
    public void init() {
        motorRight1 = hardwareMap.dcMotor.get("motor_2b");
        motorRight2 = hardwareMap.dcMotor.get("motor_2a");
        motorLeft1 = hardwareMap.dcMotor.get("motor_1a");
        motorLeft2 = hardwareMap.dcMotor.get("motor_1b");

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
            break;
        }
    }

    @Override
    public void loop() {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        int ix,iy;
        double joy1y1 = gamepad1.left_stick_y;
        double joy1x1 = gamepad1.left_stick_x;

        for (iy = 0; iy<5; iy++) {
            if (joy1y1 < yAxisMatrix[iy]) {
                break;
            }
        }
        for (ix = 0; ix<5; ix++) {
            if (joy1x1 < xAxisMatrix[ix]) {
                break;
            }
        }

        motorRight1.setPower(rearRightMatrix[ix][iy]);
        motorLeft1.setPower(frontLeftMatrix[ix][iy]);
        motorRight2.setPower(frontRightMatrix[ix][iy]);
        motorLeft2.setPower(rearLeftMatrix[ix][iy]);
        // clip the right/left values so that the values never exceed +/- 1
//        longitude = Range.clip(longitude, -1, 1);
//        latitude = Range.clip(latitude, -1, 1);
//        long1 = Range.clip(long1, -1, 1);
//        long2 = Range.clip(long2, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
//        longitude = (float) scaleInput(longitude);
//        latitude = (float) scaleInput(latitude);

        //This is terrible code and I hate it but its ONLY TEMPORARY until the steering redesign is complete
//        if (long1 <= -0.75 || long2 <= -0.75) {
//            motorLeft1.setPower(1);
//            motorRight1.setPower(-1);
//            motorLeft2.setPower(-1);
//            motorRight2.setPower(1);
//        }
//        else if (long1 >= 0.75 || long2 >= 0.75) {
//            motorLeft1.setPower(-1);
//            motorRight1.setPower(1);
//            motorLeft2.setPower(1);
//            motorRight2.setPower(-1);
//        }
//        else {
//            motorRight1.setPower(longitude);
//            motorLeft1.setPower(latitude);
//            motorRight2.setPower(longitude);
//            motorLeft2.setPower(latitude);
//        }

        if(gamepad1.a == true) {
            precisionModeDrive = 1;
        }

        if(gamepad2.a == true) {
            precisionModeArm = 1;
        }

        if(gamepad1.b == true) {

            precisionModeDrive = 0;
        }

        if(gamepad2.b == true) {
            precisionModeArm = 0;
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
    }

    /*
	 * This method scales the joystick input so for low joystick values, the
	 * scaled value is less than linear.  This is to make it easier to drive
	 * the robot more precisely at slower speeds.
	 */
    double scaleInput(double dVal)  {
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
    }
}

