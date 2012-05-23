package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class ExitAction extends ParentAbstractAction
{
	private static final long	serialVersionUID	= 7496527303982095411L;

	public ExitAction(MainFrame mainFrame)
	{
		super(mainFrame, "Exit", null);
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		mainController.exit();
	}

}
