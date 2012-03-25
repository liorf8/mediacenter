package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;

public class ExitAction implements ActionListener
{
	@Override
	public void actionPerformed(ActionEvent e)
	{
		MainController.getInstance().exit();
	}

}
