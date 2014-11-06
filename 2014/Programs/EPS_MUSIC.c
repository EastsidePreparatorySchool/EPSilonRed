#include "EPS_MUSIC.h"

void PlayNote (int index, float beats, float pause)
{
	playImmediateTone (((float)200*(pow(1.08333333,index))), (beats - pause)*25);
	writeDebugStreamLine ("Freq: %f", ((float)300*(pow(1.08333333,index))));
	if (pause > 0)
	{
		wait10Msec ((beats - pause) * 25);
		clearSounds();
		wait10Msec (pause * 25);
	}
	else
	{
		wait10Msec (beats * 25);
	}

}

void EPS_WeAreTheChampions (void)
{
	clearSounds();
	PlayNote (10, 4, 0.3);
	PlayNote (9, 1, 0);
	PlayNote (10, 1, 0);
	PlayNote (9, 3, 0.3);
	PlayNote (6, 3, 0);
}

void EPS_Housework (void)
{
	clearSounds();

	PlayNote (10, 0.5, 0.2);
	PlayNote (10, 0.5, 0.2);
	PlayNote (10, 0.5, 0.2);
	PlayNote (10, 1, 0.2);
	PlayNote (10, 0.5, 0.2);
	PlayNote (8,1, 0.2);
	PlayNote (11, 1, 0.2);
}
