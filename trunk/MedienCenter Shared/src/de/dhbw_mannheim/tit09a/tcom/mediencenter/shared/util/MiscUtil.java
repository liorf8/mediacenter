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
}
