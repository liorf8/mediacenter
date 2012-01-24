package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.VLCController;

public class ExitAppAction extends AbstractMediaCenterAction
{
    private static final long serialVersionUID = -1158390380672398820L;

    public ExitAppAction()
    {
	super("Beenden");
    }

    @Override
    public void startAction()
    {
	try
	{
	    this.setEnabled(false);
	    System.out.println("Exiting...");
	    VLCController.getInstance(false).releaseResources();
	    System.exit(0);
	}
	catch (Exception e)
	{
	    this.setEnabled(true);
	}

    }

}
