package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;

public class IOUtil
{
	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static String convertStreamToString(InputStream is) throws IOException
	{
		/*
		 * To convert the InputStream to String we use the Reader.read(char[] buffer) method. We iterate until the Reader return -1 which means
		 * there's no more data to read. We use the StringWriter class to produce the string.
		 */
		if (is != null)
		{
			Writer writer = new StringWriter();

			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			}
			finally
			{
				is.close();
			}
			return writer.toString();
		}
		else
		{
			System.err.println("!!! InputStream was " + is);
			return "";
		}
	}

	// --------------------------------------------------------------------------------
	/**
	 * @param clazz The class which ClassLoader has access to the resource.
	 * @param uri
	 * @return
	 * @throws IOException
	 */
	public static String resourceToString(Class<?> clazz, URI uri) throws IOException
	{
		return resourceToString(clazz, uri.getPath());
	}

	// --------------------------------------------------------------------------------
	private static String resourceToString(Class<?> clazz, String path) throws IOException
	{
		return convertStreamToString(clazz.getResourceAsStream(path));
	}

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private IOUtil()
	{

	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
