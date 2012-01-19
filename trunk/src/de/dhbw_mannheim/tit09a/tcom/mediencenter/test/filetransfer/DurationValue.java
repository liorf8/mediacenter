package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

public class DurationValue
{
    public static final long ONE_MILLI = 1L;
    public static final long ONE_SEC = ONE_MILLI * 1000L;
    public static final long ONE_MIN = ONE_SEC * 60L;
    public static final long ONE_HOUR = ONE_MIN * 60L;

    public static enum DurationUnit
    {
	MILLISECOND(ONE_MILLI), SECOND(ONE_SEC), MINUTE(ONE_MIN), HOUR(ONE_HOUR);

	private final long millisInUnit;

	private DurationUnit(long millisInUnit)
	{
	    this.millisInUnit = millisInUnit;
	}

	public long getMillisInUnit()
	{
	    return millisInUnit;
	}
    }

    private long totalMillis;

    public DurationValue(long totalMillis)
    {
	this.totalMillis = totalMillis;
    }

    public void setTotalMillis(long totalMillis)
    {
	this.totalMillis = totalMillis;
    }

    public long getTotalMillis()
    {
	return totalMillis;
    }

    public String toString()
    {
	return formatMillis(getTotalMillis(), true, true);
    }

    public String toString(long millis, boolean skipEmptyHours)
    {
	return toString(millis, skipEmptyHours);
    }

    public static String formatMillis(long millis, boolean skipEmptyHours, boolean noMillis)
    {
	String formattedString;

	// e.g. 00:23:00,001 or 100:23:00,001
	formattedString = String.format("%02d:%02d:%02d,%03d",
		getAmountOfUnit(millis, DurationUnit.HOUR),
		getAmountOfUnit(millis, DurationUnit.MINUTE),
		getAmountOfUnit(millis, DurationUnit.SECOND),
		getAmountOfUnit(millis, DurationUnit.MILLISECOND));

	if (skipEmptyHours)
	{
	    formattedString = formattedString.replaceFirst("^0+:", "");
	}
	if (noMillis)
	{
	    formattedString = formattedString.replaceFirst(",\\d+", "");
	}

	return formattedString;
    }

    public static long getAmountOfUnit(long millis, DurationUnit unit)
    {
	if (unit.ordinal() >= DurationUnit.values().length - 1)
	{
	    return millis / unit.getMillisInUnit();
	}
	else
	{
	    return millis % DurationUnit.values()[unit.ordinal() + 1].getMillisInUnit()
		    / unit.getMillisInUnit();
	}
    }
}
