package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.media;

import uk.co.caprica.vlcj.player.VideoTrackInfo;

public class ExtendedVideoTrackInfo extends VideoTrackInfo
{
	private static final long	serialVersionUID	= -5714240364318422963L;

	private final String		description;

	public ExtendedVideoTrackInfo(VideoTrackInfo vti, String description)
	{
		this(vti.codec(), vti.id(), vti.profile(), vti.level(), vti.width(), vti.height(), description);
	}
	
	public ExtendedVideoTrackInfo(int codec, int id, int profile, int level, int width, int height, String description)
	{
		super(codec, id, profile, level, width, height);
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
}
