package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by supertrevor on 2015-11-13.
 */

public class EPSArmTest extends OpMode {
    DcMotor motorExtension;
    DcMotor motorAngle;

    @Override
    public void init() {
        motorExtension = hardwareMap.dcMotor.get("motor_3");
        motorAngle = hardwareMap.dcMotor.get("motor_4");
    }

    @Override
    public void start() {
        motorAngle.setDirection(DcMotor.Direction.FORWARD);
        motorExtension.setDirection(DcMotor.Direction.FORWARD);
    }

    @Override
    public void loop() {
        /*
		 * Gamepad 1
		 *
		 * Gamepad 1 controls the motors via the left stick, and it controls the
		 * wrist/claw via the a,b, x, y buttons
		 */

        // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
        // 1 is full down
        // direction: left_stick_x ranges from -1 to 1, where -1 is full left
        // and 1 is full right
        float extendor = -gamepad1.right_stick_y;
        float angler = gamepad1.left_stick_y;

        // clip the right/left values so that the values never exceed +/- 1
        angler = Range.clip(angler, -1, 1);
        extendor = Range.clip(extendor, -1, 1);

        // scale the joystick value to make it easier to control
        // the robot more precisely at slower speeds.
        angler = (float)scaleInput(angler);
        extendor =  (float)scaleInput(extendor);

        // write the values to the motors
        motorExtension.setPower(extendor);
        motorAngle.setPower(angler);
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