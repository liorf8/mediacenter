package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.app;

import javax.swing.Icon;


public class PlayMediaAction extends AbstractMediaCenterAction
{
    private static final long serialVersionUID = 9169956882963597514L;

    public PlayMediaAction()
    {
	super("Wiedergabe");
    }

    public PlayMediaAction(String actionName, Icon imgIcon)
    {
	super(actionName, imgIcon);
	// TODO Auto-generated constructor stub
    }

    @Override
    public void startAction()
    {
	VLCController.getInstance(true).getMediaPlayer().play();
    }

}
