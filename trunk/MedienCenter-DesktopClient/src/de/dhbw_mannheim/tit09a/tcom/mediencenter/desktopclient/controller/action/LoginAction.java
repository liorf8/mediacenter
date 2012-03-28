package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.TaskPanel;

public class LoginAction extends AbstractSwingWorkerAction<Void, String>
{
	private final MainFrame	mainFrame;

	public LoginAction(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
	}

	@Override
	protected SwingWorker<Void, String> buildSwingWorker(ActionEvent e)
	{
		LoginTask task = new LoginTask();
		return task;
	}

	public class LoginTask extends SwingWorker<Void, String> implements ActionListener
	{
		private TaskPanel	taskPanel;

		public LoginTask()
		{
			taskPanel = new TaskPanel(this);
			this.addPropertyChangeListener(new PropertyChangeListener()
			{
				public void propertyChange(PropertyChangeEvent evt)
				{
					// executed in AWTEventQueue
					if ("progress".equals(evt.getPropertyName()))
					{
						taskPanel.setProgress((Integer) evt.getNewValue());
					}
				}
			});
		}

		@Override
		protected Void doInBackground() throws Exception
		{
			mainFrame.addTaskPanel(taskPanel);
			publish("Logging in...");
			mainFrame.getLoginPanel().setActionEnabled(false);

			String login = mainFrame.getLoginPanel().getLoginText();
			String pw = String.valueOf(mainFrame.getLoginPanel().getPwChars());

			if (login.isEmpty() || pw.isEmpty())
				throw new IllegalArgumentException("Not all required fields are filled!");

			MainController.getInstance().getSimonConnection().connect();
			setProgress(50);
			MainController.getInstance().getSimonConnection().login(login, pw, MainController.getInstance().getClientCallback());
			return null;
		}

		protected void process(List<String> statuses)
		{
			// executed in AWTEventQueue
			taskPanel.setStatus(statuses.get(statuses.size() - 1));
		}

		@Override
		protected void done()
		{
			// executed in AWTEventQueue
			try
			{
				get();
				setProgress(100);
				taskPanel.setStatus("Successfully logged in!");
			}
			catch (ExecutionException e)
			{
				taskPanel.setTaskFailed("Login failed: " + e.getCause().getClass().getSimpleName() + ": " + e.getCause().getMessage());
			}
			catch (CancellationException e)
			{
				taskPanel.setTaskFailed("Login cancelled!");
			}
			catch (InterruptedException e)
			{
				taskPanel.setTaskFailed("Login interrupted!");
			}
			finally
			{
				MainController.getInstance().getScheduledEventDispatcher().submit(new Runnable()
				{
					@Override
					public void run()
					{
						for (int i = 3; i > 0; i--)
						{
							taskPanel.setCancelText(i + "");
							try
							{
								Thread.sleep(1000);
							}
							catch (InterruptedException ignore)
							{}
						}
						mainFrame.removeTaskPanel(taskPanel);
						mainFrame.getLoginPanel().setActionEnabled(true);
					}
				});
			}
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			this.cancel(true);
		}
	}

}
