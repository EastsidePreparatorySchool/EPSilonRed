
task main()
{
	while(true)
	{
		int ScaleForMotor(int joyValue)
		{
			const int DEADZONE = 5;
			if(abs(joyvalue) < DEADZONE)
			{
				return 0;
			}
			if(joy1x1 > 100)
			{
				return 100;
			}
					if(joy1x2 > 100)
			{
				return 100;
			}
					if(joy1y1 > 100)
			{
				return 100;
			}
					if(joy1y2> 100)
			{
				return 100;
			}

		}
	}
}
