package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionState;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.ServerConnectionStateListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class ServerConnectionStateListerImpl implements ServerConnectionStateListener
{
	private MainController	mainController;
	private MainFrame		mainFrame;

	public ServerConnectionStateListerImpl(MainController mainController, MainFrame mainFrame)
	{
		this.mainController = mainController;
		this.mainFrame = mainFrame;
	}

	@Override
	public void stateChanged(ServerConnectionState oldState, ServerConnectionState newState)
	{
		// TODO Auto-generated method stub

	}

}
