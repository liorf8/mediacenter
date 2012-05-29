package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.listener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class SettingsPropertyChangeLister implements PropertyChangeListener
{
	private MainController	mainController;
	private MainFrame		mainFrame;

	public SettingsPropertyChangeLister(MainController mainController, MainFrame mainFrame)
	{
		this.mainController = mainController;
		this.mainFrame = mainFrame;
	}

	public SettingsPropertyChangeLister()
	{
		// TODO Auto-generated constructor stub
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
		// TODO Auto-generated method stub

	}

}
