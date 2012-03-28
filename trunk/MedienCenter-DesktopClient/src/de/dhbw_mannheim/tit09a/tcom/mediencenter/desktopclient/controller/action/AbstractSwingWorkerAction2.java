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

public abstract class AbstractSwingWorkerAction2 implements ActionListener
{
	protected MainFrame						mainFrame;
	protected AbstractSwingWorkerAction2	instance;

	public AbstractSwingWorkerAction2(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
		this.instance = this;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		new ActionTask(e).execute();
	}
	
	protected abstract void doInBackground() throws Exception;

	public class ActionTask extends SwingWorker<Void, String> implements ActionListener
	{
		private final ActionEvent	actionEvent;
		private final TaskPanel		taskPanel;

		public ActionTask(ActionEvent actionEvent)
		{
			this.actionEvent = actionEvent;
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

		protected void process(List<String> statuses)
		{
			// executed in AWTEventQueue
			taskPanel.setStatus(statuses.get(statuses.size() - 1));
		}

		@Override
		protected Void doInBackground() throws Exception
		{
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void done()
		{
			// executed in AWTEventQueue
			try
			{
				get();
				setProgress(100);
				taskPanel.setStatus(actionEvent.getActionCommand() + " successfull!");
			}
			catch (ExecutionException e)
			{
				taskPanel.setTaskFailed(actionEvent.getActionCommand() + " failed: " + e.getCause().getClass().getSimpleName() + ": "
						+ e.getCause().getMessage());
			}
			catch (CancellationException e)
			{
				taskPanel.setTaskFailed(actionEvent.getActionCommand() + " cancelled!");
			}
			catch (InterruptedException e)
			{
				taskPanel.setTaskFailed(actionEvent.getActionCommand() + " interrupted!");
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
