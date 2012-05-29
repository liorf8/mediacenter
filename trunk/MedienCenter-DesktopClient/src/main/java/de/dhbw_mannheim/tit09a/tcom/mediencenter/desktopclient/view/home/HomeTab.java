package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home;

import javax.swing.Icon;
import javax.swing.JButton;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.LogoutAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class HomeTab extends Tab
{
	private static final long	serialVersionUID	= 1L;

	public HomeTab(MainFrame mainFrame)
	{
		super(mainFrame, "Home");
		
		JButton logoutBtn = new JButton("Logout");
		logoutBtn.setAction(new LogoutAction(mainFrame));
		add(logoutBtn);
	}


	@Override
	public String getTip()
	{
		return "The home";
	}


	@Override
	public Icon getIcon()
	{
		return MainController.getInstance().getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Home Tab.png");
	}
}
