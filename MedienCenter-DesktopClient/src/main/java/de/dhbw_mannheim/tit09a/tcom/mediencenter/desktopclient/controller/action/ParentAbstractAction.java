package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public abstract class ParentAbstractAction extends AbstractAction
{

	private static final long	serialVersionUID	= -6266311717464911819L;

	protected MainController	mainController;
	protected MainFrame			mainFrame;

	public ParentAbstractAction(MainFrame mainFrame)
	{
		this.mainController = MainController.getInstance();
		this.mainFrame = mainFrame;

	}

	public void setName(String name)
	{
		putValue(Action.NAME, name);
	}
	
	public void setActionCommand(String actionCommand)
	{
		putValue(Action.ACTION_COMMAND_KEY, actionCommand);
	}
	
	public void setSmallIcon(Icon smallIcon)
	{
		putValue(Action.SMALL_ICON, smallIcon);
	}
	
	public void setSmallIcon(String smallIconUri)
	{
		putValue(Action.SMALL_ICON, mainController.getImageIcon(smallIconUri));
	}
	
	public void setLargeIcon(Icon largeIcon)
	{
		putValue(Action.LARGE_ICON_KEY, largeIcon);
	}
	
	public void setLargeIcon(String largeIconUri)
	{
		putValue(Action.SMALL_ICON, mainController.getImageIcon(largeIconUri));
	}
	
	public void setShortDescription(String shortDescription)
	{
		putValue(Action.SHORT_DESCRIPTION, shortDescription);
	}

}
