package de.dhbw_mannheim.tit09a.tcom.mediencenter.app.modell.listener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.app.view.MainFrame;
import uk.co.caprica.vlcj.binding.internal.libvlc_media_t;
import uk.co.caprica.vlcj.player.MediaPlayer;
import uk.co.caprica.vlcj.player.MediaPlayerEventListener;

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
	// TODO Auto-generated method stub

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
	MainFrame.getInstance().getTimeLabel().setCurrentTime(newTime);
    }

    @Override
    public void positionChanged(MediaPlayer mediaPlayer, float newPosition)
    {
	MainFrame.getInstance().setTimeSliderValue((int) (newPosition * MainFrame.TIME_SLIDE_MAX));

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

}
