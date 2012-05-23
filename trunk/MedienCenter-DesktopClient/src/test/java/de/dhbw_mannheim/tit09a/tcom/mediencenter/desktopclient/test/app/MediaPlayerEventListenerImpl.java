package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.app;

import java.awt.Dimension;

import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;
import uk.co.caprica.vlcj.player.TrackInfo;

public class MediaPlayerEventListenerImpl implements MediaPlayerEventListener
{

	public MediaPlayerEventListenerImpl()
	{

	}

	@Override
	public void mediaChanged(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void opening(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void buffering(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void playing(MediaPlayer mediaPlayer)
	{
		System.out.println("playing() " + mediaPlayer);
		for (TrackInfo ti : mediaPlayer.getTrackInfo())
		{
			System.out.println(ti.toString());
		}
	}

	@Override
	public void paused(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void stopped(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void forward(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void backward(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void finished(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void timeChanged(MediaPlayer mediaPlayer, long newTime)
	{
		Frame.getInstance().getTimeLabel().setCurrentTime(newTime);
	}

	@Override
	public void positionChanged(MediaPlayer mediaPlayer, float newPosition)
	{
		Frame.getInstance().setTimeSliderValue((int) (newPosition * Frame.TIME_SLIDE_MAX));

	}

	@Override
	public void seekableChanged(MediaPlayer mediaPlayer, int newSeekable)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void pausableChanged(MediaPlayer mediaPlayer, int newSeekable)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void titleChanged(MediaPlayer mediaPlayer, int newTitle)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void snapshotTaken(MediaPlayer mediaPlayer, String filename)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void lengthChanged(MediaPlayer mediaPlayer, long newLength)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void error(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaMetaChanged(MediaPlayer mediaPlayer, int metaType)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaSubItemAdded(MediaPlayer mediaPlayer, libvlc_media_t subItem)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaDurationChanged(MediaPlayer mediaPlayer, long newDuration)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaParsedChanged(MediaPlayer mediaPlayer, int newStatus)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaFreed(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void mediaStateChanged(MediaPlayer mediaPlayer, int newState)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void newMedia(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void subItemPlayed(MediaPlayer mediaPlayer, int subItemIndex)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void subItemFinished(MediaPlayer mediaPlayer, int subItemIndex)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void endOfSubItems(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void videoOutput(MediaPlayer mediaPlayer, int newCount)
	{
		// adjust JSlider
		long durationInMillis = mediaPlayer.getLength();
		System.out.println("length:" + durationInMillis);
		Frame.getInstance().getTimeLabel().setFullTime(durationInMillis);

		// adjust CanvasSize
		System.out.println("media played");

		Dimension size = mediaPlayer.getVideoDimension();
		System.out.println("size:" + size);
		if (size != null)
		{
			Frame.getInstance().setVideoCanvasSize(size);
		}

		Frame.getInstance().pack();

	}

}
