package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.util.Arrays;

public class MiscUtil
{

	private MiscUtil()
	{

	}

	public static void ensureValidString(String filename, char[] forbiddenChars) throws IllegalArgumentException
	{
		for (char oneChar : forbiddenChars)
		{
			if (filename.indexOf(oneChar) > -1)
				throw new IllegalArgumentException("Filename '" + filename + "' contains illegal char '" + oneChar + "'. Illegal chars: "
						+ Arrays.toString(forbiddenChars));
		}
	}
	
	public static boolean checkStringLength(String test, int minLength, int maxLength)
	{
		if (test == null)
			return false;
		if (test.length() < minLength || test.length() > maxLength)
			return false;
		return true;
	}
	
	public static boolean checkLongRange(long l, long lowerLimit, long upperLimit)
	{
		if (l < lowerLimit || l > upperLimit)
			return false;
		return true;
	}
}
