package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class LogoutAction extends ParentAbstractAction
{
	private static final long	serialVersionUID	= 9141023757384563043L;

	public LogoutAction(MainFrame mainFrame)
	{
		super(mainFrame, "Logout", null);
		this.mainFrame = mainFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		mainController.getSimonConnection().disconnect();
	}

}
