package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;

public class ChangeLoginAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 3764670800584379655L;

	public ChangeLoginAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setName("Change login");
		setActionCommand("Change login");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new ChangeLoginWorker(mainFrame, action, e);
	}

	private class ChangeLoginWorker extends AbstractTaskPanelSwingWorker
	{
		public ChangeLoginWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			String currentPw = mainFrame.getHomeTab().getCurrentPw1();
			String newLogin = mainFrame.getHomeTab().getNewLogin();

			if (currentPw.isEmpty() || newLogin.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			setProgress(10);

			Session session = mainController.getServerConnection().getSession();

			publish("Waiting for user input");
			int returnValue = JOptionPane.showConfirmDialog(mainFrame.getHomeTab(),
					"Do you really want to change your login from " + session.getLogin() + " to " + newLogin + "?",
					"Change login?",
					JOptionPane.YES_NO_OPTION);
			if (returnValue == JOptionPane.YES_OPTION)
			{
				setProgress(30);
				publish("Requesting change of login to " + newLogin);
				session.changeLogin(newLogin, currentPw);
			}
			else
			{
				this.cancel(false);
			}

		}
	}
}
