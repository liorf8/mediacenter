package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.LoginPanel;

public class RegisterAction extends AbstractSwingWorkerAction<Void, String>
{
	private final LoginPanel	loginDialog;

	public RegisterAction(LoginPanel loginDialog)
	{
		this.loginDialog = loginDialog;
	}

	@Override
	protected SwingWorker<Void, String> buildSwingWorker(ActionEvent e)
	{
		return new RegisterTask();
	}

	public class RegisterTask extends SwingWorker<Void, String>
	{
		@Override
		protected Void doInBackground() throws Exception
		{
			publish("Registering...");
			loginDialog.setActionEnabled(false);
			String login = loginDialog.getLoginText();
			String pw = String.valueOf(loginDialog.getPwChars());
			String pwRepeat = String.valueOf(loginDialog.getPwRepeatChars());
			
			if( login.isEmpty() || pw.isEmpty() || pwRepeat.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			if(!pw.equals(pwRepeat))
				throw new IllegalArgumentException("Password and repeated password are not equal!");
			
			MainController.getInstance().getSimonConnection().connect();
			long id =  MainController.getInstance().getSimonConnection().getServer().register(login, pw);
			
			publish("Registered " +login+ " (ID: "+id+")");
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
				System.out.println("Registering interrupted!");
			}
			catch (ExecutionException e)
			{
				System.out.println("Registering failed: " + e.getCause().getClass().getSimpleName() + ": " +e.getCause().getMessage());
			}
			catch (CancellationException e)
			{
				System.out.println("Registering cancelled!");
			}
			finally
			{
				loginDialog.setActionEnabled(true);
			}
		}
	}
}
