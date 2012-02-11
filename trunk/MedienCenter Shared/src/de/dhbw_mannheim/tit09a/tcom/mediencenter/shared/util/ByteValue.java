package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.text.DecimalFormat;

public class ByteValue
{
	public static final long			ONE_B				= 1L;
	public static final long			ONE_KB				= 1024L;
	public static final long			ONE_MB				= 1024L * 1024L;
	public static final long			ONE_GB				= 1024L * 1024L * 1024L;
	public static final long			ONE_TB				= 1024L * 1024L * 1024L * 1024L;
	public static final long			ONE_PB				= 1024L * 1024L * 1024L * 1024L * 1024L;
	public static final long			ONE_EB				= 1024L * 1024L * 1024L * 1024L * 1024L * 1024L;
	// more than 8.0 exabyte is not possible with long

	public static final DecimalFormat	twoDecPlacesNoZeros	= new DecimalFormat("#.##");

	public static enum ByteUnit
	{
		BYTE(ONE_B, ByteUnitAbbr.B),
		KILOBYTE(ONE_KB, ByteUnitAbbr.KB),
		MEGABYTE(ONE_MB, ByteUnitAbbr.MB),
		GIGABYTE(ONE_GB, ByteUnitAbbr.GB),
		TERABYTE(ONE_TB, ByteUnitAbbr.TB),
		PETABYTE(ONE_PB, ByteUnitAbbr.PB),
		EXABYTE(ONE_EB, ByteUnitAbbr.EB);

		private final ByteUnitAbbr	abbr;
		private final long			bytesInUnit;

		private ByteUnit(long bytesInUnit, ByteUnitAbbr abbr)
		{
			this.bytesInUnit = bytesInUnit;
			this.abbr = abbr;
		}

		public ByteUnitAbbr getAbbr()
		{
			return abbr;
		}

		public long getBytesInUnit()
		{
			return bytesInUnit;
		}

		public static enum ByteUnitAbbr
		{
			B, KB, MB, GB, TB, PB, EB
		}

		public String toString()
		{
			return super.toString().substring(0, 1) + super.toString().substring(1).toLowerCase();
		}
	}

	private long	bytes;

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
		return this.bytes;
	}

	public float getBytes(ByteUnit unit)
	{
		return bytesToByteUnit(this.bytes, unit);
	}

	public static float bytesToByteUnit(long bytes, ByteUnit unit)
	{
		return (float) bytes / unit.getBytesInUnit();
	}

	public static String bytesToByteUnitString(long bytes, ByteUnit unit, boolean abbr)
	{
		return String.format("%s %s", twoDecPlacesNoZeros.format(bytesToByteUnit(bytes, unit)), abbr ? unit.getAbbr() : unit);
	}

	public static String bytesToString(long bytes)
	{
		return bytesToString(bytes, true);
	}

	public static String bytesToString(long bytes, boolean abbr)
	{
		if (bytes >= ONE_EB)
		{
			return bytesToByteUnitString(bytes, ByteUnit.EXABYTE, abbr);
		}
		else if (bytes >= ONE_PB)
		{
			return bytesToByteUnitString(bytes, ByteUnit.PETABYTE, abbr);
		}
		else if (bytes >= ONE_TB)
		{
			return bytesToByteUnitString(bytes, ByteUnit.TERABYTE, abbr);
		}
		else if (bytes >= ONE_GB)
		{
			return bytesToByteUnitString(bytes, ByteUnit.GIGABYTE, abbr);
		}
		else if (bytes >= ONE_MB)
		{
			return bytesToByteUnitString(bytes, ByteUnit.MEGABYTE, abbr);
		}
		else if (bytes >= ONE_KB)
		{
			return bytesToByteUnitString(bytes, ByteUnit.KILOBYTE, abbr);
		}
		else
		{
			return bytesToByteUnitString(bytes, ByteUnit.BYTE, abbr);
		}
	}

	public String toString()
	{
		return bytesToString(this.bytes, true);
	}
}
