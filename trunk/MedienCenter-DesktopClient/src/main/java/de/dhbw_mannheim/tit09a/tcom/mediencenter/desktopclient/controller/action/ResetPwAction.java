package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.home.LoginTab;

public class ResetPwAction extends AbstractSwingWorkerAction<Void, String>
{
	private static final long	serialVersionUID	= -6498708485503830785L;

	public ResetPwAction(MainFrame mainFrame)
	{
		super(mainFrame, "Reset Password", null);
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action,  ActionEvent e)
	{
		return new RegisterWorker(mainFrame, action, e);
	}

	private class RegisterWorker extends AbstractTaskPanelSwingWorker
	{
		public RegisterWorker (MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			LoginTab loginPanel = mainFrame.getLoginTab();
			String login = loginPanel.getLoginText();
			
			if(login.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			
			publish("Waiting for user input");
			int returnValue = JOptionPane.showConfirmDialog(loginPanel, "Do you really want to reset your password?", "Reset password?", JOptionPane.YES_NO_OPTION);
			if(returnValue == JOptionPane.YES_OPTION)
			{
				publish("Connecting");
				mainController.getSimonConnection().connect();
				setProgress(50);
				
				publish("Resetting password");
				mainController.getSimonConnection().getServer().resetPw(login);
				System.out.println("Reset password for " + login + ". Please ask the server admin for the new one.");
			}
			else
			{
				this.cancel(false);
			}
		}
	}
}