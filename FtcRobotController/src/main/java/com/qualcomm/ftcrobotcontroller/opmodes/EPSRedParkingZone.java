package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by supertrevor on 2015-11-13.
 */

public class EPSRedParkingZone extends LinearOpMode {

    final static double MOTOR_POWER = 0.50; // Higher values will cause the robot to move faster

    DcMotor motorRight;
    DcMotor motorLeft;

    @Override
    public void runOpMode() throws InterruptedException {

        // set up the hardware devices we are going to use
        motorLeft = hardwareMap.dcMotor.get("motor_2");
        motorRight = hardwareMap.dcMotor.get("motor_1");

        motorLeft.setDirection(DcMotor.Direction.REVERSE);
        motorRight.setDirection(DcMotor.Direction.FORWARD);

        // wait for the start button to be pressed
        waitForStart();

        motorRight.setPower(MOTOR_POWER);
        motorLeft.setPower(MOTOR_POWER);

        sleep(2000);

        motorLeft.setPower(-MOTOR_POWER);

        sleep(1000);

        motorLeft.setPower(MOTOR_POWER);

        sleep(9000);

        // stop the motors
        motorRight.setPower(0);
        motorLeft.setPower(0);
    }
}

