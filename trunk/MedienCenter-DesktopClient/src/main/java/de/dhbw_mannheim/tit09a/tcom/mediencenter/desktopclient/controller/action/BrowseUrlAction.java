package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.net.URI;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class BrowseUrlAction extends AbstractSwingWorkerAction<Void, String>
{
	private static final long	serialVersionUID	= -4338302650114771616L;

	public BrowseUrlAction(MainFrame mainFrame)
	{
		super(mainFrame, "BrowseUrlAction", null);
	}

	public BrowseUrlAction(MainFrame mainFrame, String actionName, Icon icon)
	{
		super(mainFrame, actionName, icon);
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new BrowseUrlWorker(mainFrame, action, e);
	}

	private class BrowseUrlWorker extends AbstractTaskPanelSwingWorker
	{
		public BrowseUrlWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			publish("Launching browser");
			Desktop desktop = Desktop.getDesktop();
			setProgress(20);

			// The actionCommand has to be the URI-String!!!
			String actionCommand = actionEvent.getActionCommand();
			URI uri = new URI(actionCommand);
			setProgress(40);
			
			desktop.browse(uri);
		}
	}

}
