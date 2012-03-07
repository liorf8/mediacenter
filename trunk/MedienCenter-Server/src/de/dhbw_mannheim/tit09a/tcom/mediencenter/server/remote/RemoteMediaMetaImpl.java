package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote;

import java.awt.image.BufferedImage;

import java.io.Serializable;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

import uk.co.caprica.vlcj.player.MediaMeta;

@SimonRemote(value = { MediaMeta.class })
public class RemoteMediaMetaImpl implements Serializable, MediaMeta, SimonUnreferenced
{
	static final long	serialVersionUID	= -1945641832827532027L;
	private MediaMeta	mediaMeta;

	public RemoteMediaMetaImpl(MediaMeta mediaMeta)
	{
		this.mediaMeta = mediaMeta;
	}

	@Override
	public void unreferenced()
	{
		ServerMain.INVOKE_LOGGER.info("Unreferenced {}", this);
		try
		{
			mediaMeta.release();
		}
		catch (Exception e)
		{
			ServerMain.INVOKE_LOGGER.error("Unreferencing of " + this + " failed", e);
		}
	}

	@Override
	public void parse()
	{
		mediaMeta.parse();
	}

	@Override
	public String getTitle()
	{
		return mediaMeta.getTitle();
	}

	@Override
	public void setTitle(String title)
	{
		mediaMeta.setTitle(title);
	}

	@Override
	public String getArtist()
	{
		return mediaMeta.getArtist();
	}

	@Override
	public void setArtist(String artist)
	{
		mediaMeta.setArtist(artist);
	}

	@Override
	public String getGenre()
	{
		return mediaMeta.getGenre();
	}

	@Override
	public void setGenre(String genre)
	{
		mediaMeta.setGenre(genre);
	}

	@Override
	public String getCopyright()
	{
		return mediaMeta.getCopyright();
	}

	@Override
	public void setCopyright(String copyright)
	{
		mediaMeta.setCopyright(copyright);
	}

	@Override
	public String getAlbum()
	{
		return mediaMeta.getAlbum();
	}

	@Override
	public void setAlbum(String album)
	{
		mediaMeta.setAlbum(album);
	}

	@Override
	public String getTrackNumber()
	{
		return mediaMeta.getTrackNumber();
	}

	@Override
	public void setTrackNumber(String trackNumber)
	{
		mediaMeta.setTrackNumber(trackNumber);
	}

	@Override
	public String getDescription()
	{
		return mediaMeta.getDescription();
	}

	@Override
	public void setDescription(String description)
	{
		mediaMeta.setDescription(description);
	}

	@Override
	public String getRating()
	{
		return mediaMeta.getRating();
	}

	@Override
	public void setRating(String rating)
	{
		mediaMeta.setRating(rating);
	}

	@Override
	public String getDate()
	{
		return mediaMeta.getDate();
	}

	@Override
	public void setDate(String date)
	{
		mediaMeta.setDate(date);
	}

	@Override
	public String getSetting()
	{
		return mediaMeta.getSetting();
	}

	@Override
	public void setSetting(String setting)
	{
		mediaMeta.setSetting(setting);
	}

	@Override
	public String getUrl()
	{
		return mediaMeta.getUrl();
	}

	@Override
	public void setUrl(String url)
	{
		mediaMeta.setUrl(url);
	}

	@Override
	public String getLanguage()
	{
		return mediaMeta.getLanguage();
	}

	@Override
	public void setLanguage(String language)
	{
		mediaMeta.setLanguage(language);
	}

	@Override
	public String getNowPlaying()
	{
		return mediaMeta.getNowPlaying();
	}

	@Override
	public void setNowPlaying(String nowPlaying)
	{
		mediaMeta.setNowPlaying(nowPlaying);
	}

	@Override
	public String getPublisher()
	{
		return mediaMeta.getPublisher();
	}

	@Override
	public void setPublisher(String publisher)
	{
		mediaMeta.setPublisher(publisher);
	}

	@Override
	public String getEncodedBy()
	{
		return mediaMeta.getEncodedBy();
	}

	@Override
	public void setEncodedBy(String encodedBy)
	{
		mediaMeta.setEncodedBy(encodedBy);
	}

	@Override
	public String getArtworkUrl()
	{
		return mediaMeta.getArtworkUrl();
	}

	@Override
	public void setArtworkUrl(String artworkUrl)
	{
		mediaMeta.setArtworkUrl(artworkUrl);
	}

	@Override
	public String getTrackId()
	{
		return mediaMeta.getTrackId();
	}

	@Override
	public void setTrackId(String trackId)
	{
		mediaMeta.setTrackId(trackId);
	}

	@Override
	public BufferedImage getArtwork()
	{
		return mediaMeta.getArtwork();
	}

	@Override
	public void save()
	{
		mediaMeta.save();
	}

	@Override
	public void release()
	{
		mediaMeta.release();
	}

}
