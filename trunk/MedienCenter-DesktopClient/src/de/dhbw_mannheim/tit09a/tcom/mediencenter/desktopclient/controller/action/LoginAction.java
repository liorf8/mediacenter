package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class LoginAction extends AbstractSwingWorkerAction<Void, String>
{
	public LoginAction(MainFrame mainFrame)
	{
		super(mainFrame);
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainController mainController, MainFrame mainFrame, ActionEvent e)
	{
		return new LoginWorker(mainController, mainFrame, e);
	}

	private class LoginWorker extends AbstractTaskPanelSwingWorker
	{
		public LoginWorker(MainController mainController, MainFrame mainFrame, ActionEvent actionEvent)
		{
			super(mainController, mainFrame, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			String login = mainFrame.getLoginPanel().getLoginText();
			String pw = String.valueOf(mainFrame.getLoginPanel().getPwChars());

			if (login.isEmpty() || pw.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			
			publish("Connecting...");
			MainController.getInstance().getSimonConnection().connect();
			setProgress(50);
			
			publish("Logging in...");
			MainController.getInstance().getSimonConnection().login(login, pw, MainController.getInstance().getClientCallback());
		}
	}
}
