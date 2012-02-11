package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.util.concurrent.TimeUnit;

public class TimeValue
{
	private long	totalMillis;

	public TimeValue(long totalMillis)
	{
		this.totalMillis = totalMillis;
	}

	public String toString()
	{
		return toString(true, true);
	}

	public String toString(boolean omitZeroDaysAndHours, boolean omitMillis)
	{
		return formatMillis(totalMillis, omitZeroDaysAndHours, omitMillis);
	}

	public static String formatMillis(long millis)
	{
		return formatMillis(millis, true, true);
	}

	public static String formatMillis(long millis, boolean omitZeroDaysAndHours, boolean omitMillis)
	{
		String formattedString;

		// e.g. 00:23:00,001 or 100:23:00,001
		formattedString = String.format("%02d:%02d:%02d:%02d", extractPartial(millis, TimeUnit.DAYS), extractPartial(millis, TimeUnit.HOURS),
				extractPartial(millis, TimeUnit.MINUTES), extractPartial(millis, TimeUnit.SECONDS));

		if (omitZeroDaysAndHours)
		{
			formattedString = formattedString.replaceFirst("^(0+:){0,2}", "");
		}
		if (!omitMillis)
		{
			formattedString += String.format(",%03d", extractPartial(millis, TimeUnit.MILLISECONDS));
		}

		return formattedString;
	}

	public static long extractPartial(long millis, TimeUnit unit)
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
