package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.LoginPanel;

public class LoginAction extends AbstractSwingWorkerAction<Void, String>
{
	private final LoginPanel	loginPanel;

	public LoginAction(LoginPanel loginDialog)
	{
		this.loginPanel = loginDialog;
	}

	@Override
	protected SwingWorker<Void, String> buildSwingWorker(ActionEvent e)
	{
		return new LoginTask();
	}

	public class LoginTask extends SwingWorker<Void, String>
	{
		@Override
		protected Void doInBackground() throws Exception
		{
			publish("Logging in...");
			loginPanel.setActionEnabled(false);

			String login = loginPanel.getLoginText();
			String pw = String.valueOf(loginPanel.getPwChars());

			if (login.isEmpty() || pw.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");

			MainController.getInstance().getSimonConnection().connect();
			MainController.getInstance().getSimonConnection().login(login, pw, MainController.getInstance().getClientCallback());
			return null;
		}

		protected void process(List<String> statuses)
		{
			System.out.println(statuses.get(statuses.size() - 1));
		}

		@Override
		protected void done()
		{
			try
			{
				get();
			}
			catch (InterruptedException e)
			{
				System.out.println("Login interrupted!");
			}
			catch (ExecutionException e)
			{
				System.out.println("Login failed: " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
			}
			catch (CancellationException e)
			{
				System.out.println("Login cancelled!");
			}
			finally
			{
				loginPanel.setActionEnabled(true);
			}
		}
	}

}
