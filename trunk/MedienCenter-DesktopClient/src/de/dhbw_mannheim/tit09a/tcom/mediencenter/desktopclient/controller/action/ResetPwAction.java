package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.LoginPanel;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public class ResetPwAction extends AbstractSwingWorkerAction<Void, String>
{
	public ResetPwAction(MainFrame mainFrame)
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
			
			if(login.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			
			publish("Waiting for user input...");
			int returnValue = JOptionPane.showConfirmDialog(loginPanel, "Do you really want to reset your password?", "Reset password?", JOptionPane.YES_NO_OPTION);
			if(returnValue == JOptionPane.YES_OPTION)
			{
				publish("Connecting...");
				MainController.getInstance().getSimonConnection().connect();
				setProgress(50);
				
				publish("Resetting password...");
				MainController.getInstance().getSimonConnection().getServer().resetPw(login);
				System.out.println("Reset password for " + login + ". Please ask the server admin for the new one.");
			}
			else
			{
				this.cancel(false);
			}
		}
	}
}
