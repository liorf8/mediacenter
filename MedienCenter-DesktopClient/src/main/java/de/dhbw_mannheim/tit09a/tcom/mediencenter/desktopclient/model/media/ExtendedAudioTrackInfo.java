package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.media;

import uk.co.caprica.vlcj.player.AudioTrackInfo;

public class ExtendedAudioTrackInfo extends AudioTrackInfo
{
	private static final long	serialVersionUID	= -730137460747303212L;

	private final String		description;

	public ExtendedAudioTrackInfo(AudioTrackInfo ati, String description)
	{
		this(ati.codec(), ati.id(), ati.profile(), ati.level(), ati.channels(), ati.rate(), description);
	}

	public ExtendedAudioTrackInfo(int codec, int id, int profile, int level, int channels, int rate, String description)
	{
		super(codec, id, profile, level, channels, rate);
		this.description = description;
	}

	public String getDescription()
	{
		return description;
	}
}
