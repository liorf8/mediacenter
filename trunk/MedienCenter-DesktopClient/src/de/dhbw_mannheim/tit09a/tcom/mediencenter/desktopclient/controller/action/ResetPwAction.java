package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.LoginPanel;

public class ResetPwAction extends AbstractSwingWorkerAction<Void, String>
{
	private final LoginPanel	loginPanel;

	public ResetPwAction(LoginPanel loginDialog)
	{
		this.loginPanel = loginDialog;
	}

	@Override
	protected SwingWorker<Void, String> buildSwingWorker(ActionEvent e)
	{
		return new ResetPwTask();
	}

	public class ResetPwTask extends SwingWorker<Void, String>
	{
		@Override
		protected Void doInBackground() throws Exception
		{
			publish("Resetting password...");
			loginPanel.setActionEnabled(false);
			String login = loginPanel.getLoginText();
			
			if(login.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			
			int returnValue = JOptionPane.showConfirmDialog(loginPanel, "Do you really want to reset your password?", "Reset password?", JOptionPane.YES_NO_OPTION);
			if(returnValue == JOptionPane.YES_OPTION)
			{
				MainController.getInstance().getSimonConnection().connect();
				MainController.getInstance().getSimonConnection().getServer().resetPw(login);

				publish("Reset password for " + login + ". Please ask the server admin for the new one.");
			}
			else
			{
				this.cancel(false);
			}

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
				System.out.println("Resetting password interrupted!");
			}
			catch (ExecutionException e)
			{
				System.out.println("Resetting password failed: " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
			}
			catch (CancellationException e)
			{
				System.out.println("Resetting password cancelled!");
			}
			finally
			{
				loginPanel.setActionEnabled(true);
			}
		}
	}
}
