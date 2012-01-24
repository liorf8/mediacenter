package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.VLCController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class StopMediaAction extends AbstractMediaCenterAction
{
    private static final long serialVersionUID = -5128526035789976256L;

    public StopMediaAction()
    {
	super("Stopp");
    }

    @Override
    public void startAction()
    {
	VLCController.getInstance(true).getMediaPlayer().stop();
	MainFrame.getInstance().setTimeSliderValue(0);

    }

}
