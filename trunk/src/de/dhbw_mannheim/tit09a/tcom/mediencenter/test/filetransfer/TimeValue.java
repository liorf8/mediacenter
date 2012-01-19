package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.util.concurrent.TimeUnit;

public class TimeValue
{
    private long totalMillis;

    public TimeValue(long totalMillis)
    {
	this.totalMillis = totalMillis;
    }

    public String toString()
    {
	return formatMillis(totalMillis, true, true);
    }

    public String toString(long millis, boolean omitZeroDaysAndHours)
    {
	return toString(millis, omitZeroDaysAndHours);
    }

    public static String formatMillis(long millis, boolean omitZeroDaysAndHours, boolean omitMillis)
    {
	String formattedString;

	// e.g. 00:23:00,001 or 100:23:00,001
	formattedString = String.format("%02d:%02d:%02d:%02d,%03d",
		getPartial(millis, TimeUnit.DAYS),
		getPartial(millis, TimeUnit.HOURS),
		getPartial(millis, TimeUnit.MINUTES),
		getPartial(millis, TimeUnit.SECONDS),
		getPartial(millis, TimeUnit.MILLISECONDS));

	if (omitZeroDaysAndHours)
	{
	    formattedString = formattedString.replaceFirst("^(0+:){0,2}", "");
	}
	if (omitMillis)
	{
	    formattedString = formattedString.replaceFirst(",\\d+", "");
	}

	return formattedString;
    }

    public static long getPartial(long millis, TimeUnit unit)
    {
	if (TimeUnit.DAYS.equals(unit))
	{
	    return unit.convert(millis, TimeUnit.MILLISECONDS);
	}
	else
	{
	    TimeUnit nextHigher = TimeUnit.values()[unit.ordinal() + 1];
	    return unit.convert((millis % nextHigher.toMillis(1)), TimeUnit.MILLISECONDS);
	}
    }
}
