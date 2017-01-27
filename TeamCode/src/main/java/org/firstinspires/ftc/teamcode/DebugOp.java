package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.GyroSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.hardware.I2cAddr;

/**
 * Created by ninjas28 on 2016-12-21.
 */

@TeleOp(name = "DebugOp", group = "EPSilon Red (8103)")
public class DebugOp extends OpMode {
    DcMotor motorRight1;
    DcMotor motorLeft1;
    DcMotor motorRight2;
    DcMotor motorLeft2;

    ColorSensor leftcolor;
    OpticalDistanceSensor leftdist;
    ColorSensor rightcolor;
    OpticalDistanceSensor rightdist;
    TouchSensor touch;

    final double[] rotationMatrix = new double[]{-0.2,-0.1, 0.0, 0.1, 0.2};

    int fastleft = 0;
    int fastright = 4;
    int slowleft = 1;
    int slowright = 3;
    int stop = 2;

    int rx = 2;
    int rxPrev = 2;

    AutoRotator rotator;

    @Override
    public void init() {
        GyroSensor gyro;

        leftcolor = hardwareMap.colorSensor.get("leftcolor");
        leftdist = hardwareMap.opticalDistanceSensor.get("leftdist");
        rightcolor = hardwareMap.colorSensor.get("rightcolor");
        rightdist = hardwareMap.opticalDistanceSensor.get("rightdist");
        gyro = hardwareMap.gyroSensor.get("gyro");
        touch = hardwareMap.touchSensor.get("touch");
        motorRight1 = hardwareMap.dcMotor.get("motor_1a");
        motorRight2 = hardwareMap.dcMotor.get("motor_1b");
        motorLeft1 = hardwareMap.dcMotor.get("motor_2b");
        motorLeft2 = hardwareMap.dcMotor.get("motor_2a");

        leftcolor.setI2cAddress(I2cAddr.create8bit(0x3c));
        rightcolor.setI2cAddress(I2cAddr.create8bit(0x4c));

        rotator = new AutoRotator(gyro, motorLeft1, motorLeft2, motorRight1, motorRight2);
        rotator.init();
    }

    @Override
    public void start() {
        //IN HONOR OF HENRY MENG'S VALIANT HUMILIATION AND USAGE OF WHILE(TRUE)
        while(true) {
            motorLeft1.setDirection(DcMotor.Direction.FORWARD);
            motorRight1.setDirection(DcMotor.Direction.REVERSE);
            motorLeft2.setDirection(DcMotor.Direction.FORWARD);
            motorRight2.setDirection(DcMotor.Direction.REVERSE);
            rotator.gyro.resetZAxisIntegrator();
            break;
        }
    }

    @Override
    public void loop() {
        //int blue = color.blue();
        int color1 = leftcolor.blue();
        int color2 = rightcolor.blue();
        //int heading = gyro.getHeading();
        int heading = rotator.gyro.getHeading();
        //int red = color.red();
        //double light = dist.getLightDetected();


        //telemetry.addData("color sensor", "blue: " + String.format("%d", blue));
        telemetry.addData("gyro", "heading: " + String.format("%d", heading));
        telemetry.addData("color", "blue: " + String.format("%d", color1));
        telemetry.addData("color2", "blue: " + String.format("%d", color2));
        telemetry.addData("leftdist", "lighting: " + String.format("%.2f", leftdist.getLightDetected()));
        telemetry.addData("rightdist", "lighting: " + String.format("%.2f", rightdist.getLightDetected()));
        //telemetry.addData("touch", "sense: " + touch.isPressed());
    }
}
