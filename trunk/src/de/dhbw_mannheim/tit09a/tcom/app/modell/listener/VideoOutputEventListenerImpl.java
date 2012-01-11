package de.dhbw_mannheim.tit09a.tcom.app.modell.listener;

import java.awt.Dimension;

import de.dhbw_mannheim.tit09a.tcom.app.view.MainFrame;

import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.events.VideoOutputEventListener;

public class VideoOutputEventListenerImpl implements VideoOutputEventListener
{
    @Override
    public void videoOutputAvailable(MediaPlayer mediaPlayer, boolean videoOutput)
    {
	//adjust JSlider
	long durationInMillis = mediaPlayer.getLength();
	System.out.println("length:" + durationInMillis);
	MainFrame.getInstance().getTimeLabel().setFullTime(durationInMillis);
	
	//adjust CanvasSize
	System.out.println("media played");
	System.out.println("videoOutput:" + videoOutput);
	if (videoOutput)
	{
	    Dimension size = mediaPlayer.getVideoDimension();
	    System.out.println("size:" + size);
	    if (size != null)
	    {
		MainFrame.getInstance().setVideoCanvasSize(size);
	    }
	}
	
	MainFrame.getInstance().pack();
    }
}
