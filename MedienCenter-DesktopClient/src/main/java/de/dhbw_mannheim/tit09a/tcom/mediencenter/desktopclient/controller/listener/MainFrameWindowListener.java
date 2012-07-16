package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action.ExitAction;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class MainFrameWindowListener implements WindowListener
{
	@SuppressWarnings("unused")
	private MainController	mainController;
	private MainFrame		mainFrame;

	public MainFrameWindowListener(MainController mainController, MainFrame mainFrame)
	{
		this.mainController = mainController;
		this.mainFrame = mainFrame;
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		mainFrame.getLoginTab().requestFocusForLogin();

	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		new ExitAction(mainFrame).actionPerformed(new ActionEvent(e.getSource(), -1, "Exit"));
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		// TODO Auto-generated method stub

	}

}
