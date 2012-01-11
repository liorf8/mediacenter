package de.dhbw_mannheim.tit09a.tcom.app.modell.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

public abstract class AbstractMediaCenterAction extends AbstractAction implements Runnable
{
    private static final long serialVersionUID = 6197832452804128043L;

    public AbstractMediaCenterAction(String actionName)
    {
	super(actionName);
    }

    public AbstractMediaCenterAction(String actionName, Icon imgIcon)
    {
	super(actionName, imgIcon);
    }


	//---------------------------------------------------------------------------
	//--- Abstract Method for Child-Classes -------------------------------------
	//---------------------------------------------------------------------------
	public abstract void startAction();

	// ----------------------------------------------------------------------------
	// -- Overwritten from 'Runnable' ---------------------------------------------
	// ----------------------------------------------------------------------------
	public void run()
	{
		startAction();
	}

	// ----------------------------------------------------------------------------
	// -- Overwritten from 'AbstractAction' ---------------------------------------
	// ----------------------------------------------------------------------------
	public void actionPerformed(ActionEvent e)
	{
		Thread actionThread = new Thread(this);
		actionThread.setPriority(Thread.NORM_PRIORITY);
		actionThread.start();
	}
	
	// ----------------------------------------------------------------------------
}
