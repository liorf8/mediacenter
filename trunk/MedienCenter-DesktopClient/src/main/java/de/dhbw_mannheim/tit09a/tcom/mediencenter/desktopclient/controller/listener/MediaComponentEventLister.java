package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener;

import javax.swing.Icon;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MediaToolBar;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

public class MediaComponentEventLister implements MediaPlayerEventListener
{

	private MainFrame	mainFrame;
	private int			timeChangedCounter;

	public MediaComponentEventLister(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
	}

	@Override
	public void mediaChanged(MediaPlayer mediaPlayer, libvlc_media_t media, String mrl)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void opening(MediaPlayer mediaPlayer)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void buffering(MediaPlayer mediaPlayer, float newCache)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void playing(MediaPlayer mediaPlayer)
	{
		System.out.println("playing()");
		Icon ico = MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Play.png");
		FileInfo currentFile = mainFrame.getScreenTab().getCurrentFile();
		MediaToolBar mediaToolBar = mainFrame.getMediaToolBar();
		mediaToolBar.setNowPlaying(ico, currentFile.getName());
	}

	@Override
	public void paused(MediaPlayer mediaPlayer)
	{
		System.out.println("paused()");
		Icon ico = MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Pause.png");
		mainFrame.getMediaToolBar().setNowPlaying(ico);

	}

	@Override
	public void stopped(MediaPlayer mediaPlayer)
	{
		System.out.println("stopped()");
		Icon ico = MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Stop.png");
		mainFrame.getMediaToolBar().setNowPlaying(ico, null);

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
		timeChangedCounter++;
		if (timeChangedCounter % 10 == 0)
		{
			long currentTime = MainController.getInstance().getServerConnection().getStreamPlayer().getTime();
			mainFrame.getMediaToolBar().setCurrentTime(currentTime);
		}

	}

	@Override
	public void positionChanged(MediaPlayer mediaPlayer, float newPosition)
	{
		// TODO Auto-generated method stub

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
	public void videoOutput(MediaPlayer mediaPlayer, int newCount)
	{
		System.out.println("videoOutput()");

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
}
