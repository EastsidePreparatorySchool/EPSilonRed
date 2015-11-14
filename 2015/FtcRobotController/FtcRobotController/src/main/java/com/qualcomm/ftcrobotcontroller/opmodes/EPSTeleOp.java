package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by supertrevor on 2015-11-13.
 */

public class EPSTeleOp extends OpMode {

    final static double MOTOR_POWER = 0.25; // Higher values will cause the robot to move faster

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
    }

    @Override
    public void loop() {
        motorLeft.setPower(MOTOR_POWER);
        motorRight.setPower(MOTOR_POWER);
    }
}

