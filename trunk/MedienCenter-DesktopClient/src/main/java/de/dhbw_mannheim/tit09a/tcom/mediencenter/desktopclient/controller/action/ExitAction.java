package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;

public class ExitAction extends AbstractAction
{
	private static final long	serialVersionUID	= 7496527303982095411L;

	public ExitAction()
	{
		super("Exit");
	}
	
	@Override
	public void actionPerformed(ActionEvent e)
	{
		MainController.getInstance().exit();
	}

}
