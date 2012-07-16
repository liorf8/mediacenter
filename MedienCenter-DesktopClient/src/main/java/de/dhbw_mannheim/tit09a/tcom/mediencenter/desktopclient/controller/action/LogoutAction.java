package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class LogoutAction extends ParentAbstractAction
{
	private static final long	serialVersionUID	= 9141023757384563043L;

	public LogoutAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setName("Logout");
		setSmallIcon(MediaUtil.PATH_IMGS_16x16 + "Logout.png");
		setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Logout.png");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		mainController.getServerConnection().disconnect();
	}

}
