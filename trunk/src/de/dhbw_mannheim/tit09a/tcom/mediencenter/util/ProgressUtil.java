package de.dhbw_mannheim.tit09a.tcom.mediencenter.util;

public class ProgressUtil
{
    public static float percentage(long done, long total)
    {
	return done * 100f / total;
    }

    public static float speed(long units, long millis)
    {
	return units / (millis/1000f);
    }
    
    public static long remainingSecs(long unitsLeft, float speed)
    {
	return (long) (unitsLeft / speed);
    }
}
