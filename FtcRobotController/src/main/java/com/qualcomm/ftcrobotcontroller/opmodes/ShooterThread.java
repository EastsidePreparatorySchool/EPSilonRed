package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * Created by ninjas28 on 2016-12-13.
 */

public class ShooterThread implements Runnable {
    DcMotor motorWinch;
    DcMotor motorCollector;
    Servo trigger;
    ServoController sc;
    Semaphore sl;
    Catapult crossbow;

    ShooterThread(DcMotor winch, DcMotor collector, Servo t, ServoController controller, Semaphore lock) {
        motorWinch = winch;
        motorCollector = collector;
        trigger = t;
        sc = controller;
        sl = lock;
        crossbow = new Catapult(winch, t);
        motorCollector.setDirection(DcMotor.Direction.REVERSE);
        motorWinch.setDirection(DcMotor.Direction.FORWARD);
        sc.pwmEnable();
    }

    public void run() {
        try {
            sl.acquire();
            crossbow.fire();
            sl.release();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
