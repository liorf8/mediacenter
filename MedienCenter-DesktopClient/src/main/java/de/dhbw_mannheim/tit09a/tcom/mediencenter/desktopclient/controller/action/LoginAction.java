package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.connection.ServerConnection;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class LoginAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 3764670800584379655L;

	public LoginAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setName("Login");
		setSmallIcon(MediaUtil.PATH_IMGS_16x16 + "Login.png");
		setLargeIcon(MediaUtil.PATH_IMGS_22x22 + "Login.png");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new LoginWorker(mainFrame, action, e);
	}

	private class LoginWorker extends AbstractTaskPanelSwingWorker
	{
		public LoginWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			String login = mainFrame.getLoginTab().getLoginText();
			String pw = String.valueOf(mainFrame.getLoginTab().getPwChars());

			if (login.isEmpty() || pw.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");

			ServerConnection simonConn = mainController.getServerConnection();

			String address = simonConn.getServerHost() + ":" + simonConn.getServerRegistryPort() + "/" + simonConn.getServerBindname();
			publish("Connecting to " + address);
			simonConn.connect();
			setProgress(50);

			publish("Logging in as " + login);
			simonConn.login(login, pw, mainController.getClientCallback());
		}
	}
}
