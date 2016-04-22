package com.qualcomm.ftcrobotcontroller.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range

abstract class ScalaTeleOp extends OpMode {
  private[opmodes] var motorRight2: DcMotor
  private[opmodes] var motorLeft1: DcMotor
  private[opmodes] var motorRight1: DcMotor
  private[opmodes] var motorLeft2: DcMotor
  private[opmodes] var precisionModeDrive: Int = 0
  private[opmodes] var precisionModeArm: Int = 0
  private[opmodes] var frontLeftMatrix: Array[Array[Int]] = Array[Array[Int]](Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0))
  private[opmodes] var frontrightMatrix: Array[Array[Int]] = Array[Array[Int]](Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0))
  private[opmodes] var rearLeftMatrix: Array[Array[Int]] = Array[Array[Int]](Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0))
  private[opmodes] var rearRightMatrix: Array[Array[Int]] = Array[Array[Int]](Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0), Array(0, 0, 0, 0, 0))

  def init {
    motorRight2 = hardwareMap.dcMotor.get("motor_2a")
    motorLeft1 = hardwareMap.dcMotor.get("motor_1a")
    motorRight1 = hardwareMap.dcMotor.get("motor_2b")
    motorLeft2 = hardwareMap.dcMotor.get("motor_1b")
    precisionModeDrive = 0
    precisionModeArm = 0
  }

  override def start {
    motorLeft1.setDirection(DcMotor.Direction.FORWARD)
    motorRight1.setDirection(DcMotor.Direction.REVERSE)
    motorLeft2.setDirection(DcMotor.Direction.FORWARD)
    motorRight2.setDirection(DcMotor.Direction.REVERSE)
  }

  def loop {
    // throttle: left_stick_y ranges from -1 to 1, where -1 is full up, and
    // 1 is full down
    // direction: left_stick_x ranges from -1 to 1, where -1 is full left
    // and 1 is full right
    var latitude: Float = -gamepad1.right_stick_y
    var longitude: Float = gamepad1.left_stick_y
    var long1: Float = gamepad1.left_stick_x
    var long2: Float = gamepad1.right_stick_x

    // clip the right/left values so that the values never exceed +/- 1
    longitude = Range.clip(longitude, -1, 1)
    latitude = Range.clip(latitude, -1, 1)
    long1 = Range.clip(long1, -1, 1)
    long2 = Range.clip(long2, -1, 1)

    // scale the joystick value to make it easier to control
    // the robot more precisely at slower speeds.
    longitude = scaleInput(longitude).toFloat
    latitude = scaleInput(latitude).toFloat

    //This is terrible code and I hate it but its ONLY TEMPORARY until the steering redesign is complete
    if (long1 <= -0.75 || long2 <= -0.75) {
      motorLeft1.setPower(1)
      motorRight1.setPower(-1)
      motorLeft2.setPower(-1)
      motorRight2.setPower(1)
    }
    else if (long1 >= 0.75 || long2 >= 0.75) {
      motorLeft1.setPower(-1)
      motorRight1.setPower(1)
      motorLeft2.setPower(1)
      motorRight2.setPower(-1)
    }
    else {
      motorRight1.setPower(longitude)
      motorLeft1.setPower(latitude)
      motorRight2.setPower(longitude)
      motorLeft2.setPower(latitude)
    }

    if (gamepad1.a == true) {
      precisionModeDrive = 1
    }

    if (gamepad2.a == true) {
      precisionModeArm = 1
    }

    if (gamepad1.b == true) {
      precisionModeDrive = 0
    }

    if (gamepad2.b == true) {
      precisionModeArm = 0
    }

    /*
     * Send telemetry data back to driver station. Note that if we are using
     * a legacy NXT-compatible motor controller, then the getPower() method
     * will return a null value. The legacy NXT-compatible motor controllers
     * are currently write only.
     */
    //telemetry.addData("Text", "*** Robot Data***")
    //telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", latitude))
    //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", longitude))
  }

  private[opmodes] def scaleInput(dVal: Double): Double = {
    val scaleArray: Array[Double] = Array(0.0, 0.05, 0.09, 0.10, 0.12, 0.15, 0.18, 0.24, 0.30, 0.36, 0.43, 0.50, 0.60, 0.72, 0.85, 1.00, 1.00)
    var index: Int = (dVal * 16.0).toInt
    if (index < 0) {
      index = -index
    }
    if (index > 16) {
      index = 16
    }
    var dScale: Double = 0.0
    if (dVal < 0) {
      dScale = -scaleArray(index)
    }
    else {
      dScale = scaleArray(index)
    }
    return dScale
  }
}