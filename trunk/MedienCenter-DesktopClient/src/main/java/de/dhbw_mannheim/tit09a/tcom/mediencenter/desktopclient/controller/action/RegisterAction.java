package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.LoginTab;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class RegisterAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= -8477787763880936893L;

	public RegisterAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setName("Register");

		setSmallIcon(MediaUtil.PATH_IMGS_16x16 + "Register.png");
		setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Register.png");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new RegisterWorker(mainFrame, action, e);
	}

	private class RegisterWorker extends AbstractTaskPanelSwingWorker
	{
		public RegisterWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			LoginTab loginPanel = mainFrame.getLoginTab();
			String login = loginPanel.getLoginText();
			String pw = String.valueOf(loginPanel.getPwChars());
			String pwRepeat = String.valueOf(loginPanel.getPwRepeatChars());

			if (login.isEmpty() || pw.isEmpty() || pwRepeat.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			if (!pw.equals(pwRepeat))
				throw new IllegalArgumentException("Password and repeated password are not equal!");

			publish("Connecting");
			mainController.getServerConnection().connect();
			setProgress(50);

			publish("Registering " + login);
			long id = mainController.getServerConnection().getServer().register(login, pw);

			System.out.println("Registered " + login + " (ID: " + id + ")");
		}
	}

}
