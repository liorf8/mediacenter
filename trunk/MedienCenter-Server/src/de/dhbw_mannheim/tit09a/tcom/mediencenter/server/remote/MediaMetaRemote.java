package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.awt.image.BufferedImage;

import java.io.Serializable;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.RemoteMediaMeta;
import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

import uk.co.caprica.vlcj.player.MediaMeta;

@SimonRemote(value = { MediaMeta.class })
public class MediaMetaRemote implements Serializable, RemoteMediaMeta, SimonUnreferenced
{
	static final long			serialVersionUID	= 1L;

	private volatile boolean	isValid				= true;
	private MediaMeta			nativeMediaMeta;

	public MediaMetaRemote(MediaMeta nativeMediaMeta)
	{
		this.nativeMediaMeta = nativeMediaMeta;
	}

	@Override
	public void unreferenced()
	{
		ServerMain.INVOKE_LOGGER.info("Unreferenced {}", this);
		try
		{
			invalidate();
		}
		catch (Exception e)
		{
			ServerMain.INVOKE_LOGGER.warn("Unreferencing of " + this + " failed", e);
		}
	}

	@Override
	public boolean isValid()
	{
		return isValid;
	}

	@Override
	public void parse()
	{
		checkValid();
		nativeMediaMeta.parse();
	}

	@Override
	public String getTitle()
	{
		checkValid();
		return nativeMediaMeta.getTitle();
	}

	@Override
	public void setTitle(String title)
	{
		checkValid();
		nativeMediaMeta.setTitle(title);
	}

	@Override
	public String getArtist()
	{
		checkValid();
		return nativeMediaMeta.getArtist();
	}

	@Override
	public void setArtist(String artist)
	{
		checkValid();
		nativeMediaMeta.setArtist(artist);
	}

	@Override
	public String getGenre()
	{
		checkValid();
		return nativeMediaMeta.getGenre();
	}

	@Override
	public void setGenre(String genre)
	{
		checkValid();
		nativeMediaMeta.setGenre(genre);
	}

	@Override
	public String getCopyright()
	{
		checkValid();
		return nativeMediaMeta.getCopyright();
	}

	@Override
	public void setCopyright(String copyright)
	{
		checkValid();
		nativeMediaMeta.setCopyright(copyright);
	}

	@Override
	public String getAlbum()
	{
		checkValid();
		return nativeMediaMeta.getAlbum();
	}

	@Override
	public void setAlbum(String album)
	{
		checkValid();
		nativeMediaMeta.setAlbum(album);
	}

	@Override
	public String getTrackNumber()
	{
		checkValid();
		return nativeMediaMeta.getTrackNumber();
	}

	@Override
	public void setTrackNumber(String trackNumber)
	{
		checkValid();
		nativeMediaMeta.setTrackNumber(trackNumber);
	}

	@Override
	public String getDescription()
	{
		checkValid();
		return nativeMediaMeta.getDescription();
	}

	@Override
	public void setDescription(String description)
	{
		checkValid();
		nativeMediaMeta.setDescription(description);
	}

	@Override
	public String getRating()
	{
		checkValid();
		return nativeMediaMeta.getRating();
	}

	@Override
	public void setRating(String rating)
	{
		checkValid();
		nativeMediaMeta.setRating(rating);
	}

	@Override
	public String getDate()
	{
		checkValid();
		return nativeMediaMeta.getDate();
	}

	@Override
	public void setDate(String date)
	{
		checkValid();
		nativeMediaMeta.setDate(date);
	}

	@Override
	public String getSetting()
	{
		checkValid();
		return nativeMediaMeta.getSetting();
	}

	@Override
	public void setSetting(String setting)
	{
		checkValid();
		nativeMediaMeta.setSetting(setting);
	}

	@Override
	public String getUrl()
	{
		checkValid();
		return nativeMediaMeta.getUrl();
	}

	@Override
	public void setUrl(String url)
	{
		checkValid();
		nativeMediaMeta.setUrl(url);
	}

	@Override
	public String getLanguage()
	{
		checkValid();
		return nativeMediaMeta.getLanguage();
	}

	@Override
	public void setLanguage(String language)
	{
		checkValid();
		nativeMediaMeta.setLanguage(language);
	}

	@Override
	public String getNowPlaying()
	{
		checkValid();
		return nativeMediaMeta.getNowPlaying();
	}

	@Override
	public void setNowPlaying(String nowPlaying)
	{
		checkValid();
		nativeMediaMeta.setNowPlaying(nowPlaying);
	}

	@Override
	public String getPublisher()
	{
		checkValid();
		return nativeMediaMeta.getPublisher();
	}

	@Override
	public void setPublisher(String publisher)
	{
		checkValid();
		nativeMediaMeta.setPublisher(publisher);
	}

	@Override
	public String getEncodedBy()
	{
		checkValid();
		return nativeMediaMeta.getEncodedBy();
	}

	@Override
	public void setEncodedBy(String encodedBy)
	{
		checkValid();
		nativeMediaMeta.setEncodedBy(encodedBy);
	}

	@Override
	public String getArtworkUrl()
	{
		checkValid();
		return nativeMediaMeta.getArtworkUrl();
	}

	@Override
	public void setArtworkUrl(String artworkUrl)
	{
		checkValid();
		nativeMediaMeta.setArtworkUrl(artworkUrl);
	}

	@Override
	public String getTrackId()
	{
		checkValid();
		return nativeMediaMeta.getTrackId();
	}

	@Override
	public void setTrackId(String trackId)
	{
		checkValid();
		nativeMediaMeta.setTrackId(trackId);
	}

	@Override
	@Deprecated
	public BufferedImage getArtwork()
	{
		throw new UnsupportedOperationException("BufferedImage is not serializable! Load the image on client side via getArtWorkUrl().");
	}

	@Override
	public void save()
	{
		checkValid();
		nativeMediaMeta.save();
	}

	@Override
	public synchronized void release()
	{
		invalidate();
	}

	public String toString()
	{
		if (isValid)
			return this.getClass().getSimpleName() + "[" + nativeMediaMeta.toString() + "]";
		else
			return this.getClass().getSimpleName() + "[invalid]";
	}

	private synchronized void invalidate()
	{
		if (isValid)
		{
			isValid = false;
			nativeMediaMeta.release();
		}
	}

	private void checkValid() throws IllegalStateException
	{
		if (!isValid)
			throw new IllegalStateException(this + " is not valid anymore.");
	}

}
