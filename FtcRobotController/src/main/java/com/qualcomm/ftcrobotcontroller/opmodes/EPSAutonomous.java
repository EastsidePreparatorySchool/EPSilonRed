package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by supertrevor on 2015-11-13.
 */

/*
 **************************************************
 * ------------------------------------------------
 * ------------DEPRECATED-DO-NOT-USE!--------------
 * ------------------------------------------------
 **************************************************
 */

public class EPSAutonomous extends OpMode {

    final static double MOTOR_POWER = 0.50; // Higher values will cause the robot to move faster

    DcMotor motorRight;
    DcMotor motorLeft;

    @Override
    public void init() {
        motorRight = hardwareMap.dcMotor.get("motor_2");
        motorLeft = hardwareMap.dcMotor.get("motor_1");
    }

    @Override
    public void start() {
        motorLeft.setDirection(DcMotor.Direction.FORWARD);
        motorRight.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop() {
        motorLeft.setPower(MOTOR_POWER);
        motorRight.setPower(MOTOR_POWER);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        motorLeft.setPower(0);
        motorRight.setPower(0);
    }
}

