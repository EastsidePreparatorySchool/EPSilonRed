package com.qualcomm.ftcrobotcontroller.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range

class ScalaTeleOp extends OpMode {
  private[opmodes] var motorRight2: DcMotor = null
  private[opmodes] var motorLeft1: DcMotor = null
  private[opmodes] var motorRight1: DcMotor = null
  private[opmodes] var motorLeft2: DcMotor = null
  private[opmodes] var motorArmAngle: DcMotor = null
  private[opmodes] var motorActuator: DcMotor = null
  private[opmodes] var motorChurroGrabber: DcMotor = null
  private[opmodes] var motorWinch: DcMotor = null
  private[opmodes] var servoPlow: Servo = null
  private[opmodes] var precisionModeDrive: Int = 0
  private[opmodes] var precisionModeArm: Int = 0

  def init {
    motorRight2 = hardwareMap.dcMotor.get("motor_2a")
    motorLeft1 = hardwareMap.dcMotor.get("motor_1a")
    motorRight1 = hardwareMap.dcMotor.get("motor_2b")
    motorLeft2 = hardwareMap.dcMotor.get("motor_1b")
   /* motorArmAngle = hardwareMap.dcMotor.get("motor_3")
    motorActuator = hardwareMap.dcMotor.get("motor_4")
    motorChurroGrabber = hardwareMap.dcMotor.get("motor_5")
    motorWinch = hardwareMap.dcMotor.get("motor_6")
    servoPlow = hardwareMap.servo.get("servo_1")*/
    precisionModeDrive = 0
    precisionModeArm = 0
  }

  override def start {
    motorLeft1.setDirection(DcMotor.Direction.FORWARD)
    motorRight1.setDirection(DcMotor.Direction.REVERSE)
    motorLeft2.setDirection(DcMotor.Direction.FORWARD)
    motorRight2.setDirection(DcMotor.Direction.REVERSE)
    /*motorArmAngle.setDirection(DcMotor.Direction.REVERSE)
    motorActuator.setDirection(DcMotor.Direction.REVERSE)
    motorChurroGrabber.setDirection(DcMotor.Direction.FORWARD)
    motorWinch.setDirection(DcMotor.Direction.FORWARD)
    servoPlow.setDirection(Servo.Direction.FORWARD)*/
  }

  def loop {
    var leftTread: Float = -gamepad1.right_stick_y
    var rightTread: Float = gamepad1.left_stick_y
    var armAngle: Float = gamepad2.right_stick_y
    var actuator: Float = gamepad2.left_stick_y
    rightTread = Range.clip(rightTread, -1, 1)
    leftTread = Range.clip(leftTread, -1, 1)
    armAngle = Range.clip(armAngle, -1, 1)
    actuator = Range.clip(actuator, -1, 1)
    rightTread = scaleInput(rightTread).toFloat
    leftTread = scaleInput(leftTread).toFloat
    armAngle = scaleInput(armAngle).toFloat
    actuator = scaleInput(actuator).toFloat
    if (precisionModeDrive == 1) {
      motorRight1.setPower(rightTread / 2f)
      motorLeft1.setPower(leftTread / 2f)
      motorRight2.setPower(rightTread / 2f)
      motorLeft2.setPower(leftTread / 2f)
    }
    else {
      motorRight1.setPower(rightTread)
      motorLeft1.setPower(leftTread)
      motorRight2.setPower(rightTread)
      motorLeft2.setPower(leftTread)
    }
    if (precisionModeArm == 1) {
      motorArmAngle.setPower(armAngle * 0.2f)
      motorActuator.setPower(actuator / 4f)
    }
    else {
      motorArmAngle.setPower(armAngle * 0.30f)
      motorActuator.setPower(actuator / 2f)
    }
    if (gamepad1.x) {
      motorChurroGrabber.setPower(1f)
    }
    else if (gamepad1.y) {
      motorChurroGrabber.setPower(-1f)
    }
    else {
      motorChurroGrabber.setPower(0)
    }
    if (gamepad1.dpad_up) {
      servoPlow.setPosition(0.1)
    }
    else if (gamepad1.dpad_down) {
      servoPlow.setPosition(1.0)
    }
    if (gamepad1.a) {
      precisionModeDrive = 1
    }
    if (gamepad2.a) {
      precisionModeArm = 1
    }
    if (gamepad1.b) {
      precisionModeDrive = 0
    }
    if (gamepad2.b) {
      precisionModeArm = 0
    }
    if (gamepad2.x) {
      motorWinch.setPower(0.9f)
    }
    else if (gamepad2.y) {
      motorWinch.setPower(-0.9f)
    }
    else {
      motorWinch.setPower(0f)
    }
    //telemetry.addData("Text", "*** Robot Data***")
    //telemetry.addData("left tgt pwr", "left  pwr: " + String.format("%.2f", leftTread))
    //telemetry.addData("right tgt pwr", "right pwr: " + String.format("%.2f", rightTread))
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