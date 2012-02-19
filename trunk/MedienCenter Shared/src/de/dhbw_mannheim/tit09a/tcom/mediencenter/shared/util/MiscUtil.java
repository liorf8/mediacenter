package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.regex.Pattern;

public class MiscUtil
{
	private MiscUtil()
	{

	}

	public static void ensureNoForbiddenChars(String string, char[] forbiddenChars) throws IllegalArgumentException
	{
		for (char oneChar : forbiddenChars)
		{
			if (string.indexOf(oneChar) > -1)
				throw new IllegalArgumentException("String '" + string + "' contains illegal char '" + oneChar + "'. Illegal chars: "
						+ Arrays.toString(forbiddenChars));
		}
	}
	
	public static void ensureValidString(String string, String regex) throws IllegalArgumentException
	{
		if(!Pattern.matches(regex, string))
			throw new IllegalArgumentException("String '"+string+"' does not match regex: " +regex);
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
	
	// --------------------------------------------------------------------------------
	public static String pathToUriString(Path path)
	{
		return path.toString().replace(FileSystems.getDefault().getSeparator(), "/");
	}

	
}
