#ifndef EPS_MUSIC
#define EPS_MUSIC
void PlayNote(int index, float beats, float pause)
{
	playImmediateTone(((float)200 * (pow(1.08333333, index))), (beats - pause) * 25);
	writeDebugStreamLine("Freq: %f", ((float)300 * (pow(1.08333333, index))));
	if (pause > 0)
	{
		wait1Msec((beats - pause) * 250);
		clearSounds();
		wait1Msec(pause * 250);
	}
	else
	{
		wait1Msec(beats * 250);
	}

}

void EPS_WeAreTheChampions(void)
{
	clearSounds();
	PlayNote(10, 4, 0.3);
	PlayNote(9, 1, 0);
	PlayNote(10, 1, 0);
	PlayNote(9, 3, 0.3);
	PlayNote(6, 3, 0);
}

void EPS_Housework(void)
{
	clearSounds();

	PlayNote(10, 0.5, 0.2);
	PlayNote(10, 0.5, 0.2);
	PlayNote(10, 0.5, 0.2);
	PlayNote(10, 1, 0.2);
	PlayNote(10, 0.5, 0.2);
	PlayNote(8, 1, 0.2);
	PlayNote(11, 1, 0.2);
}
#endif
