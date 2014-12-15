#ifndef EPS_DRIVER
#define EPS_DRIVER
int slow = 1;
void EPS_driver_control()
{
	int joyvalueX[] = { -75, -40, 40, 75 };
	int joyvalueY[] = { -75, -40, 40, 75 };

	int leftMotorMatrix[5] = {-50, -25, 0, 25, 50};

	int rightMotorMatrix[5] = { -50, -25, 0, 25, 50};
	int joy1x1, joy1y1, joy1y2, joy2dpad1;
	while (true)
	{
		int ix, iy;
		getJoystickSettings(joystick);
		joy1y2 = joystick.joy1_y2;
		joy1x1 = joystick.joy1_x1;
		joy1y1 = joystick.joy1_y1;
		joy2dpad1 = joystick.joy2_TopHat;

		if (joystick.StopPgm) {
			break;
		}
		for (iy = 0; iy<4; iy++) {
			if (joy1y2 < joyvalueY[iy]) {
				break;
			}
		}
		for (ix = 0; ix<4; ix++) {
			if (joy1x1 < joyvalueX[ix]) {
				break;
			}
		}
		if (abs(joy1y2) < 15) {
			motor[motorD] = leftMotorMatrix[ix] / slow;
			motor[motorE] = leftMotorMatrix[ix] / slow;
		}
		if (abs(joy1x1) < 15) {
			motor[motorD] = -1*leftMotorMatrix[iy] / slow;
			motor[motorE] = rightMotorMatrix[iy] / slow;
		}

		servoChangeRate[servo1] = 0;
		servoChangeRate[servo2] = 0;
		servoChangeRate[servo3] = 0;

		if (joy2dpad1 == -1) {
			motor[motorF] = 0;
			motor[motorG] = 0;
			//PlayNote (10, 4, 0.3);
		}
		if (joy2dpad1 == 4) {
			motor[motorF] = 100;
			motor[motorG] = 70;
			//PlayNote (9, 1, 0);
		}
		if (joy2dpad1 == 0) {
			motor[motorF] = -100;
			motor[motorG] = -70;
			//PlayNote (9, 1, 0);
		}
		if (joy2Btn(4) == 1) {
			motor[motorF] = -50;
			motor[motorG] = 0;
			//PlayNote (9, 1, 0);
		}
		//precision mode
		if (joy1Btn(6) == 1) {
			if(slow == 3)
			{
				slow=1;
			}
			else{
				slow = 3;
			}

			//PlayNote (9, 1, 0);
		}

		if (joy2Btn(1) == 1) {
			motor[motorF] = 0;
			motor[motorG] = -50;
			//PlayNote (9, 1, 0);
		}
		if (joy2Btn(3) == 1) {
			motor[motorF] = 50;
			motor[motorG] = 0;
			//PlayNote (9, 1, 0);
		}
		if (joy2Btn(2) == 1) {
			motor[motorF] = 0;
			motor[motorG] = 50;
			//PlayNote (9, 1, 0);
		}
		//
		// Spinner
		//
		if (joy2Btn(7) == 1) {
			servo[servo4] = 255;
		}
		if (joy2Btn(8) == 1) {
			servo[servo4] = 0;
		}
		if (joy2Btn(7) != 1 && joy2Btn(8) != 1) {
			servo[servo4] = 128;
		}
		//
		// goal finger
		//
		if (joy1Btn(2) == 1) {
			servo[servo3] = 128;
		}
		if (joy1Btn(3) == 1) {
			servo[servo3] = 0;
		}
		wait1Msec(20);
	}
	motor[motorD] = 0;
	motor[motorE] = 0;
}
#endif
