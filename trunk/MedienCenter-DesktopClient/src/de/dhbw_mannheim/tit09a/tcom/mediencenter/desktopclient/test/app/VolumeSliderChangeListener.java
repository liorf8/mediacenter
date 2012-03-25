package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.app;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


public class VolumeSliderChangeListener implements ChangeListener
{

    @Override
    public void stateChanged(ChangeEvent e)
    {
	JSlider source = (JSlider) e.getSource();
	int volume = source.getValue();
	VLCController.getInstance(true).getMediaPlayer().setVolume(volume);
	Frame.getInstance().setVolumeToolTip(""+volume);
    }

}
