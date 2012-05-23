package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.play;

import java.awt.Canvas;
import java.awt.Color;

import javax.swing.JFrame;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.embedded.DefaultFullScreenStrategy;
import uk.co.caprica.vlcj.player.embedded.FullScreenStrategy;

public class MediaComponent extends EmbeddedMediaPlayerComponent
{
	private static final long	serialVersionUID	= 1L;

	private JFrame				frame;

	private Canvas				canvas;

	public MediaComponent(JFrame frame)
	{
		this.frame = frame;
	}

	@Override
	protected FullScreenStrategy onGetFullScreenStrategy()
	{
		return new DefaultFullScreenStrategy(frame);
	}

	@Override
	protected Canvas onGetCanvas()
	{
		canvas = new Canvas();
		canvas.setBackground(Color.black);
		return canvas;
	}

	@Override
	public void videoOutput(MediaPlayer mediaPlayer, int newCount)
	{
		System.out.println("videoOutput: " + newCount);
	}

	@Override
	public void playing(MediaPlayer mediaPlayer)
	{
		System.out.println("playing");
	}

	@Override
	public void error(MediaPlayer mediaPlayer)
	{
		System.out.println("error");
	}

	@Override
	public void finished(MediaPlayer mediaPlayer)
	{
		System.out.println("finished");
	}

}
