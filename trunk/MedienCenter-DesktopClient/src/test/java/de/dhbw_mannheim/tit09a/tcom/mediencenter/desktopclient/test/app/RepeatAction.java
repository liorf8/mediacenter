package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test.app;

import uk.co.caprica.vlcj.player.list.MediaListPlayerMode;

public class RepeatAction extends AbstractMediaCenterAction
{
    private static final long serialVersionUID = -6114108093751167800L;
    //private static enum REPEAT_MODE { REPEAT_ONE, REPEAT_ALL };
    
    public RepeatAction()
    {
	super("Wiederholen");
    }

    @Override
    public void startAction()
    {

	VLCController.getInstance(true).getMediaListPlayer().setMode(MediaListPlayerMode.REPEAT);
	
    }

}
