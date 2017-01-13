package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;

/**
 * Created by ninjas28 on 2016-12-21.
 */

public class AutoRotator {
    GyroSensor gyro;
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorRight2;
    DcMotor motorLeft2;

    final double[] rotationMatrix = new double[]{-0.3,-0.1, 0.0, 0.1, 0.3};
    int rx = 2;
    int rxPrev = 2;

    public AutoRotator(GyroSensor gyroscope, DcMotor left1, DcMotor left2, DcMotor right1, DcMotor right2) {
        gyro = gyroscope;
        motorRight1 = right1;
        motorLeft1 = left1;
        motorRight2 = right2;
        motorLeft2 = left2;
    }

    public void init() {
        gyro.calibrate();
        while(gyro.isCalibrating()) {}
        gyro.resetZAxisIntegrator();
    }

    //Automatically rotate to heading, user doesn't decide direction of rotation or speed.
    public void rotate(int degrees) {
        int heading = gyro.getHeading();

        while(heading > (degrees + 1) || heading < (degrees - 1)) {
            if(Math.abs(heading - degrees) < 30) {
                if(heading - degrees < 0){
                    rx = 3;
                }
                else {
                    rx = 1;
                }
                if (rxPrev != rx){
                    motorRight1.setPower(-1.0 * rotationMatrix[rx]);
                    motorLeft1.setPower(rotationMatrix[rx]);
                    motorRight2.setPower(-1.0 * rotationMatrix[rx]);
                    motorLeft2.setPower(rotationMatrix[rx]);
                    rxPrev = rx;
                }
            } else if (Math.abs(heading - degrees) > 30) {
                if(heading - degrees < 0){
                    rx = 4;
                }
                else {
                    rx = 0;
                }
                if (rxPrev != rx){
                    motorRight1.setPower(-1.0 * rotationMatrix[rx]);
                    motorLeft1.setPower(rotationMatrix[rx]);
                    motorRight2.setPower(-1.0 * rotationMatrix[rx]);
                    motorLeft2.setPower(rotationMatrix[rx]);
                    rxPrev = rx;
                }
            }
            heading = gyro.getHeading();
        }
        rx = 2;
        if (rxPrev != rx){
            motorRight1.setPower(-1.0 * rotationMatrix[rx]);
            motorLeft1.setPower(rotationMatrix[rx]);
            motorRight2.setPower(-1.0 * rotationMatrix[rx]);
            motorLeft2.setPower(rotationMatrix[rx]);
            rxPrev = rx;
        }
    }

    //Manually rotate to heading, user-defined direction and speed
    public void manuallyRotate(int degrees, String direction, double motorSpeed) {
        int heading = gyro.getHeading();

        if(direction.toLowerCase() == "left") {
            motorRight1.setPower(Math.abs(motorSpeed));
            motorLeft1.setPower(-1.0 * Math.abs(motorSpeed));
            motorRight2.setPower(Math.abs(motorSpeed));
            motorLeft2.setPower(-1.0 * Math.abs(motorSpeed));
        }
        if (direction.toLowerCase() == "right"){
            motorRight1.setPower(-1.0 * Math.abs(motorSpeed));
            motorLeft1.setPower(Math.abs(motorSpeed));
            motorRight2.setPower(-1.0 * Math.abs(motorSpeed));
            motorLeft2.setPower(Math.abs(motorSpeed));
        }
        while(heading > (degrees + 2) || heading < (degrees - 2)) {
            heading = gyro.getHeading();
        }
        motorRight1.setPower(0.0);
        motorLeft1.setPower(0.0);
        motorRight2.setPower(0.0);
        motorLeft2.setPower(0.0);
    }

    public void driveStraight(double startPower) {

    }
}
