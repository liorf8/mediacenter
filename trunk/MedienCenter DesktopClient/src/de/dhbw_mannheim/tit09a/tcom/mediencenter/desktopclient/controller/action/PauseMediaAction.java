package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import javax.swing.Icon;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.VLCController;

public class PauseMediaAction extends AbstractMediaCenterAction
{

    /**
     * 
     */
    private static final long serialVersionUID = 2940816193668984516L;

    public PauseMediaAction()
    {
	super("Pause");
    }

    public PauseMediaAction(String actionName, Icon imgIcon)
    {
	super(actionName, imgIcon);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void startAction()
    {
	VLCController.getInstance(true).getMediaPlayer().pause();

    }

}
