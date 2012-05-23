package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.Tab;

public class HomeTab extends Tab
{
	private static final long	serialVersionUID	= 1L;

	public HomeTab(JFrame frame)
	{
		super("Home");
		
		JButton logoutBtn = new JButton("Logout");
		logoutBtn.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e)
			{
				MainController.getInstance().getSimonConnection().disconnect();
				
			}});
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
		// TODO Auto-generated method stub
		return null;
	}
}
