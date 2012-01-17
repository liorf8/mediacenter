package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.text.DecimalFormat;

public class ByteValue
{
    public static final Long ONE_KB = 1024L;
    public static final Long ONE_MB = 1024L * 1024L;
    public static final Long ONE_GB = 1024L * 1024L * 1024L;
    public static final Long ONE_TB = 1024L * 1024L * 1024L * 1024L;
    public static final Long ONE_PB = 1024L * 1024L * 1024L * 1024L * 1024L;

    public static final DecimalFormat noZeros = new DecimalFormat("#.#");

    public static enum ByteUnit
    {
	Byte, Kilobyte, Megabyte, Gigabyte, Terabyte, Petabyte
    }
    
    public static enum ByteUnitAbbreviation
    {
	B, KB, MB, GB, TB, PB
    }

    private long bytes;

    public ByteValue(long bytes)
    {
	this.bytes = bytes;
    }

    public void setBytes(long bytes)
    {
	this.bytes = bytes;
    }
    
    public long getBytes()
    {
	return bytes;
    }

    public String toString()
    {
	if (bytes > ONE_PB)
	{
	    return String
		    .format("%s %s", noZeros.format((float) bytes / ONE_PB), ByteUnit.Petabyte);
	}
	else if (bytes > ONE_TB)
	{
	    return String
		    .format("%s %s", noZeros.format((float) bytes / ONE_TB), ByteUnit.Terabyte);
	}
	else if (bytes > ONE_GB)
	{
	    return String
		    .format("%s %s", noZeros.format((float) bytes / ONE_GB), ByteUnit.Gigabyte);
	}
	else if (bytes > ONE_MB)
	{
	    return String
		    .format("%s %s", noZeros.format((float) bytes / ONE_MB), ByteUnit.Megabyte);
	}
	else if (bytes > ONE_KB)
	{
	    return String
		    .format("%s %s", noZeros.format((float) bytes / ONE_KB), ByteUnit.Kilobyte);
	}
	else
	{
	    return String.format("%s %s", noZeros.format((float) bytes), ByteUnit.Byte);
	}
    }
}
