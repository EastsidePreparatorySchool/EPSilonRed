#ifndef EPS_DRIVER
#define EPS_DRIVER
void EPS_driver_control()
{
	int joyvalueX[] = { -75, -40, 40, 75 };
	int joyvalueY[] = { -75, -40, 40, 75 };

	int leftMotorMatrix[5][5] = { { -50, -25, 50, 0, 0 },
	{ -15, 10, 25, 0, 0 },
	{ 25, 0, 0, 0, -25 },
	{ -15, 0, -25, 0, 0 },
	{ 0, 0, -25, 0, 0 } };
	int rightMotorMatrix[5][5] = { { 0, 0, -50, 25, 50 },
	{ 0, 0, -25, 25, 15 },
	{ 25, 0, 0, 0, -25 },
	{ 0, 0, 25, 0, 15 },
	{ 0, 0, 50, 0, -25 } };
	//EPS_Housework();
	while (true)
	{
		int ix, iy, joy1x1, joy1y1, joy2y2;
		getJoystickSettings(joystick);
		joy2y2 = joystick.joy1_y2;
		joy1x1 = joystick.joy1_x1;
		joy1y1 = joystick.joy1_y1;
		short btn1 = joy1Btn(1);
		short btn3 = joy1Btn(3);
		short btn6 = joy1Btn(6);
		short btn7 = joy1Btn(7);
		short btn2 = joy1Btn(2);

		if (joystick.StopPgm) {
			break;
		}
		for (iy = 0; iy<4; iy++) {
			if (joy1y1 < joyvalueY[iy]) {
				break;
			}
		}
		for (ix = 0; ix<4; ix++) {
			if (joy1x1 < joyvalueX[ix]) {
				break;
			}
		}
		motor[motorD] = leftMotorMatrix[iy][ix];
		motor[motorE] = rightMotorMatrix[iy][ix];

		// servo control
		//

		//int buttons = joystick.joy1_Buttons;

		/*if (joy2y2 > 3)
		{
			servo[servo1] = SCOOP_DOWN;

		}
		if (joy2y2 < -3)
		{
			servo[servo1] = SCOOP_UP;
		}

		if (btn1 > 0) {
			servoChangeRate[servo1] = 10;
		}

		if (btn3 > 0) {
			servoChangeRate[servo1] = 5;
		}
		if (btn6 != 0) {
			motor[motorA] = 100;
		}
		else if (btn7 != 0) {
		motor[motorA] = -100;
		}*/
		if (btn2 > 0) {
			motor[motorA] = 0;
			motor[motorD] = 0;
			motor[motorE] = 0;
		}

		wait1Msec(4);
	}
	motor[motorA] = 0;
	motor[motorD] = 0;
	motor[motorE] = 0;
}
#endif
