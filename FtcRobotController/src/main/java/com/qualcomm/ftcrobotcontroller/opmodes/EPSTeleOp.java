package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by supertrevor on 2015-11-13.
 */

public class EPSTeleOp extends OpMode {
    DcMotor motorRight;
    DcMotor motorLeft;

    DcMotor motorArmAngle;
    DcMotor motorActuator;

    DcMotor motorChurroGrabber;

    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
        motorArmAngle = hardwareMap.dcMotor.get("motor_3");
        motorActuator = hardwareMap.dcMotor.get("motor_4");
        motorChurroGrabber = hardwareMap.dcMotor.get("motor_5");
    }

    @Override
    public void start() {
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
        motorRight.setDirection(DcMotor.Direction.FORWARD);

        motorArmAngle.setDirection(DcMotor.Direction.REVERSE);
        motorActuator.setDirection(DcMotor.Direction.REVERSE);

        motorChurroGrabber.setDirection((DcMotor.Direction.FORWARD));
    }

    @Override
    public void loop() {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float leftTread = -gamepad1.right_stick_y;
        float rightTread = gamepad1.left_stick_y;
        float armAngle = gamepad2.right_stick_y;
        float actuator = gamepad2.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        rightTread = Range.clip(rightTread, -1, 1);
        leftTread = Range.clip(leftTread, -1, 1);
        armAngle = Range.clip(armAngle, -1, 1);
        actuator = Range.clip(actuator, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        rightTread = (float)scaleInput(rightTread);
        leftTread =  (float)scaleInput(leftTread);
        armAngle = (float)scaleInput(armAngle);
        actuator =  (float)scaleInput(actuator);

        // write the values to the motors
        motorRight.setPower(rightTread);
        motorLeft.setPower(leftTread);
        motorArmAngle.setPower(armAngle * 0.5f);
        motorActuator.setPower(actuator);

        if(gamepad1.x == true)
        {
            motorChurroGrabber.setPower(0.25f);
        }

        else if(gamepad1.y == true)
        {
            motorChurroGrabber.setPower(-0.25f);
        }

        else
        {
            motorChurroGrabber.setPower(0);
        }





		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", leftTread));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightTread));
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

