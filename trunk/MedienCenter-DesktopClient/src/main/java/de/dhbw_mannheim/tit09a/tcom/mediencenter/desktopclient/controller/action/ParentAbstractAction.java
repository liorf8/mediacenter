package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public abstract class ParentAbstractAction extends AbstractAction
{

	private static final long	serialVersionUID	= -6266311717464911819L;

	protected MainController	mainController;
	protected MainFrame			mainFrame;

	public ParentAbstractAction(MainFrame mainFrame, String name, Icon icon)
	{
		super(name, icon);
		this.mainFrame = mainFrame;
		this.mainController = MainController.getInstance();
	}

}
