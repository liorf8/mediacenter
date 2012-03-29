package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.LoginPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class RegisterAction extends AbstractSwingWorkerAction<Void, String>
{
	public RegisterAction(MainFrame mainFrame)
	{
		super(mainFrame);
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainController mainController, MainFrame mainFrame, ActionEvent e)
	{
		return new RegisterWorker(mainController, mainFrame, e);
	}

	private class RegisterWorker extends AbstractTaskPanelSwingWorker
	{
		public RegisterWorker(MainController mainController, MainFrame mainFrame, ActionEvent actionEvent)
		{
			super(mainController, mainFrame, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			LoginPanel loginPanel = mainFrame.getLoginPanel();
			String login = loginPanel.getLoginText();
			String pw = String.valueOf(loginPanel.getPwChars());
			String pwRepeat = String.valueOf(loginPanel.getPwRepeatChars());

			if (login.isEmpty() || pw.isEmpty() || pwRepeat.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			if (!pw.equals(pwRepeat))
				throw new IllegalArgumentException("Password and repeated password are not equal!");

			publish("Connecting...");
			MainController.getInstance().getSimonConnection().connect();
			setProgress(50);
			
			publish("Registering...");
			long id = MainController.getInstance().getSimonConnection().getServer().register(login, pw);

			System.out.println("Registered " + login + " (ID: " + id + ")");
		}
	}

}
