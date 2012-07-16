package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

public class NetUtil
{
	
	public static String buildImdbSearch(String movie)
	{
		try
		{
			return "http://www.imdb.com/find?q=" + URLEncoder.encode(movie, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public static String buildFilmstartsSearch(String movie)
	{
		try
		{
			return "http://www.filmstarts.de/suche/?q=" + URLEncoder.encode(movie, "UTF-8");
		}
		catch (UnsupportedEncodingException e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	
	public static URI buildNewVideoUri(String movie) throws URISyntaxException
	{
		return new URI(buildNewVideo(movie));
	}

	public static String buildNewVideo(String movie)
	{
		return "http://www.new-video.de/film-" + movie.replace(" ", "-").replace("ä", "ae").replace("ü", "ue").replace("ö", "oe").replace("ß", "ss");
	}

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

	private NetUtil()
	{

	}

}
