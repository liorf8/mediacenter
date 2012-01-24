package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener;

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.VLCController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class VolumeSliderChangeListener implements ChangeListener
{

    @Override
    public void stateChanged(ChangeEvent e)
    {
	JSlider source = (JSlider) e.getSource();
	int volume = source.getValue();
	VLCController.getInstance(true).getMediaPlayer().setVolume(volume);
	MainFrame.getInstance().setVolumeToolTip(""+volume);
    }

}
