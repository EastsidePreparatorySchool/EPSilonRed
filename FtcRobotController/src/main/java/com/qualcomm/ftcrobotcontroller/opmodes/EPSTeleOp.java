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

    /*DcMotor motorArmAngle;
    DcMotor motorActuator;

    DcMotor motorChurroGrabber;
    DcMotor motorWinch;

    Servo servoPlow;*/

    int precisionModeDrive;
    int precisionModeArm;

    @Override
    public void init() {
        motorRight1 = hardwareMap.dcMotor.get("motor_2b");
        motorRight2 = hardwareMap.dcMotor.get("motor_2a");
        motorLeft1 = hardwareMap.dcMotor.get("motor_1a");
        motorLeft2 = hardwareMap.dcMotor.get("motor_1b");
        /*motorArmAngle = hardwareMap.dcMotor.get("motor_3");
        motorActuator = hardwareMap.dcMotor.get("motor_4");
        motorChurroGrabber = hardwareMap.dcMotor.get("motor_5");
        motorWinch = hardwareMap.dcMotor.get("motor_6");
        servoPlow = hardwareMap.servo.get("servo_1");*/

        precisionModeDrive = 0;
        precisionModeArm = 0;
    }

    @Override
    public void start() {
        motorLeft1.setDirection(DcMotor.Direction.FORWARD);
        motorRight1.setDirection(DcMotor.Direction.FORWARD);
        motorLeft2.setDirection(DcMotor.Direction.REVERSE);
        motorRight2.setDirection(DcMotor.Direction.REVERSE);

       /* motorArmAngle.setDirection(DcMotor.Direction.REVERSE);
        motorActuator.setDirection(DcMotor.Direction.REVERSE);

        motorChurroGrabber.setDirection((DcMotor.Direction.FORWARD));
        motorWinch.setDirection((DcMotor.Direction.FORWARD));

        servoPlow.setDirection(Servo.Direction.FORWARD);
*/
    }

    @Override
    public void loop() {

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float latitude = -gamepad1.right_stick_y;
        float longitude = gamepad1.right_stick_x;
        float armAngle = gamepad2.right_stick_y;
        float actuator = gamepad2.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        longitude = Range.clip(longitude, -1, 1);
        latitude = Range.clip(latitude, -1, 1);
        armAngle = Range.clip(armAngle, -1, 1);
        actuator = Range.clip(actuator, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        longitude = (float)scaleInput(longitude);
        latitude =  (float)scaleInput(latitude);
        armAngle = (float)scaleInput(armAngle);
        actuator =  (float)scaleInput(actuator);

        // write the values to the motors
        if(precisionModeDrive == 1) {
            motorRight1.setPower(longitude / 2f);
            motorLeft1.setPower(latitude / 2f);
            motorRight2.setPower(longitude / 2f);
            motorLeft2.setPower(latitude / 2f);
        }

        else {
            motorRight1.setPower(longitude);
            motorLeft1.setPower(latitude);
            motorRight2.setPower(longitude);
            motorLeft2.setPower(latitude);
        }

        /*if(precisionModeArm == 1) {
            motorArmAngle.setPower(armAngle * 0.2f);
            motorActuator.setPower(actuator / 4f);
        }

        else {
            motorArmAngle.setPower(armAngle * 0.30f);
            motorActuator.setPower(actuator / 2f);
        }*/


       /* if(gamepad1.x == true)
        {
            motorChurroGrabber.setPower(1f);
        }

        else if(gamepad1.y == true)
        {
            motorChurroGrabber.setPower(-1f);
        }

        else
        {
            motorChurroGrabber.setPower(0);
        }

        if(gamepad1.dpad_up == true) {
            servoPlow.setPosition(0.1);
        }

        else if(gamepad1.dpad_down == true) {
            servoPlow.setPosition(1.0);
        }
        */
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

       /* if(gamepad2.x == true) {
            motorWinch.setPower(0.9f);
        }

        else if (gamepad2.y == true){
            motorWinch.setPower(-0.9f);
        }

        else {
            motorWinch.setPower(0f);
        }*/

		/*
		 * Send telemetry data back to driver station. Note that if we are using
		 * a legacy NXT-compatible motor controller, then the getPower() method
		 * will return a null value. The legacy NXT-compatible motor controllers
		 * are currently write only.
		 */
        telemetry.addData("Text", "*** Robot Data***");
        telemetry.addData("left tgt pwr",  "left  pwr: " + String.format("%.2f", latitude));
        telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", longitude));
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

