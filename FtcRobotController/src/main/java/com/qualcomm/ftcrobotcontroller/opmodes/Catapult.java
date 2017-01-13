package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import java.util.concurrent.TimeUnit;

/**
 * Created by ninjas28 on 2016-12-14.
 */

public class Catapult {
    DcMotor winch;
    Servo t;
    boolean isFiring;
    public Catapult(DcMotor winchMotor, Servo trigger){
        winch = winchMotor;
        t = trigger;
        isFiring = false;
    }
    public void fire() throws InterruptedException {
        isFiring = true;
        winch.setPower(0.6);
        TimeUnit.SECONDS.sleep(2);
        winch.setPower(0.0);
        t.setPosition(0.3);
        winch.setPower(-0.3);
        TimeUnit.MILLISECONDS.sleep(1750);
        winch.setPower(0.0);
        t.setPosition(0.07);
        isFiring = false;
    }

    public boolean isFiring() {
        return isFiring;
    }
}
