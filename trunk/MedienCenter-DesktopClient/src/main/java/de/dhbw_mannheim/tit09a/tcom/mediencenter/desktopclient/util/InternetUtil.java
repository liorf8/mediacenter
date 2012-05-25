package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InternetUtil
{
	public static String buildLastFmArtist(String artist)
	{
		try
		{
			return "http://www.last.fm/music/" + URLEncoder.encode(artist, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static URI buildLastFmArtistUri(String artist) throws URISyntaxException
	{
		return new URI(buildLastFmArtist(artist));
	}

	public static String buildLastFmTitle(String artist, String title)
	{
		try
		{
			return "http://www.last.fm/music/" + URLEncoder.encode(artist, "UTF-8") + "/_/" + URLEncoder.encode(title, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static URI buildLastFmTitleUri(String artist, String title) throws URISyntaxException
	{
		return new URI(buildLastFmTitle(artist, title));
	}

	public static String buildLastFmAlbum(String artist, String album)
	{
		try
		{
			return "http://www.last.fm/music/" + URLEncoder.encode(artist, "UTF-8") + "/" + URLEncoder.encode(album, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static URI buildLastFmAlbumUri(String artist, String album) throws URISyntaxException
	{
		return new URI(buildLastFmAlbum(artist, album));
	}

	public static Image getAlbumCoverFromLastFm(String artist, String album)
	{
		InputStream is = null;
		try
		{
			URL url = buildLastFmAlbumUri(artist, album).toURL();
			is = url.openStream();
			// System.out.println(new Scanner(is).useDelimiter("\\Z").next());

			Scanner scanner = new Scanner(is);
			Pattern pAlbumCover = Pattern.compile(".*<span id=\"albumCover\".*?><img.*?src=\"(.*?)\".*");
			Matcher mAlbumCover = pAlbumCover.matcher("");
			while (scanner.hasNextLine())
			{
				String newLine = scanner.nextLine();
				mAlbumCover.reset(newLine);
				if (mAlbumCover.find())
				{
					// System.out.println("FOUND:\n" + mAlbumCover.group());
					// System.out.println("FOUND:\n" + mAlbumCover.group(1));
					return Toolkit.getDefaultToolkit().getImage(new URL(mAlbumCover.group(1)));
				}

				/*
				 * http://www.lastfm.de/music/Lily+Allen/It%27s+Not+Me,+It%27s+You
				 * 
				 * ...
				 * 
				 * <span id="albumCover" itemprop="image" class="albumCover coverMega"><img width="174"
				 * src="http://userserve-ak.last.fm/serve/174s/72329452.png" class="art" id="albumCover" itemprop="image"
				 * class="albumCover coverMega"/><span class="jewelcase"></span></span>
				 * 
				 * ...
				 */
			}
		}
		catch (URISyntaxException | IOException e)
		{
			return null;
		}
		finally
		{
			if (is != null)
				try
				{
					is.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
		return null;
	}

	private InternetUtil()
	{

	}

}
