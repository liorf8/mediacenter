package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;

public class ChangePwAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 3764670800584379655L;

	public ChangePwAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setName("Change password");
		setActionCommand("Change password");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new ChangePwWorker(mainFrame, action, e);
	}

	private class ChangePwWorker extends AbstractTaskPanelSwingWorker
	{
		public ChangePwWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			String currentPw = mainFrame.getHomeTab().getCurrentPw2();
			String newPw = mainFrame.getHomeTab().getNewPw();

			if (currentPw.isEmpty() || newPw.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");
			setProgress(10);

			Session session = mainController.getServerConnection().getSession();

			publish("Waiting for user input");
			int returnValue = JOptionPane.showConfirmDialog(mainFrame.getHomeTab(),
					"Do you really want to change your password?",
					"Change login?",
					JOptionPane.YES_NO_OPTION);
			if (returnValue == JOptionPane.YES_OPTION)
			{
				setProgress(30);
				publish("Requesting change of password");
				session.changePw(newPw, currentPw);
			}
			else
			{
				this.cancel(false);
			}

		}
	}
}
