package com.qualcomm.ftcrobotcontroller.opmodes

import com.qualcomm.robotcore.eventloop.opmode.OpMode
import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.Gamepad
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.util.Range
import util.control.Breaks._

class ScalaTeleOp extends OpMode {
  private[opmodes] var motorRight1: DcMotor = hardwareMap.dcMotor.get("motor_2b")
  private[opmodes] var motorRight2: DcMotor = hardwareMap.dcMotor.get("motor_2a")
  private[opmodes] var motorLeft1: DcMotor = hardwareMap.dcMotor.get("motor_1a")
  private[opmodes] var motorLeft2: DcMotor = hardwareMap.dcMotor.get("motor_1b")
  private[opmodes] var precisionModeDrive: Int = 0
  private[opmodes] var precisionModeArm: Int = 0
  private[opmodes] val yAxisMatrix: Array[Double] = Array[Double](-0.75, -0.40, 0, 0.40, 0.75)
  private[opmodes] val xAxisMatrix: Array[Double] = Array[Double](-0.75, -0.40, 0, 0.40, 0.75)
  private[opmodes] val frontLeftMatrix: Array[Array[Double]] = Array[Array[Double]](Array(0, 0, 1, 0.5, 1), Array(0, 0, 0.5, 0.5, 0.5), Array(-1, -0.5, 0, 0.5, 1), Array(-0.5, -0.5, -0.5, 0, 0), Array(-1, -0.5, -1, 0, 0))
  private[opmodes] val frontRightMatrix: Array[Array[Double]] = Array[Array[Double]](Array(1, 0.5, 1, 0, 0), Array(0.5, 0.5, 0.5, 0, 0), Array(1, 0.5, 0, -0.5, -1), Array(0, 0, -0.5, -0.5, -0.5), Array(0, 0, -1, -0.5, -1))
  private[opmodes] val rearLeftMatrix: Array[Array[Double]] = Array[Array[Double]](Array(1, 0.5, 1, 0, 0), Array(0.5, 0.5, 0.5, 0, 0), Array(1, 0.5, 0, -0.5, -1), Array(0, 0, -0.5, -0.5, -0.5), Array(0, 0, -1, -0.5, -1))
  private[opmodes] val rearRightMatrix: Array[Array[Double]] = Array[Array[Double]](Array(0, 0, 1, 0.5, 1), Array(0, 0, 0.5, 0.5, 0.5), Array(-1, -0.5, 0, 0.5, 1), Array(-0.5, -0.5, -0.5, 0, 0), Array(-1, -0.5, -1, 0, 0))

  def init {
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
    var ix: Int = 0
    var iy: Int = 0
    val joy1y1: Double = gamepad1.left_stick_y
    val joy1x1: Double = gamepad1.left_stick_x
    breakable {
      iy = 0
      while (iy < 5) {
        if (joy1y1 < yAxisMatrix(iy)) {
          break
        }
        iy += 1
      }
    }
    breakable {
      ix = 0
      while (ix < 5) {
        if (joy1x1 < xAxisMatrix(ix)) {
          break
        }
        ix += 1;
      }
    }
    motorRight1.setPower(rearRightMatrix(ix)(iy))
    motorLeft1.setPower(frontLeftMatrix(ix)(iy))
    motorRight2.setPower(frontRightMatrix(ix)(iy))
    motorLeft2.setPower(rearLeftMatrix(ix)(iy))
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