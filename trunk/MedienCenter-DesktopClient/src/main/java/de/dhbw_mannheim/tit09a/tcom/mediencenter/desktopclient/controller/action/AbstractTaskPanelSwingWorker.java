package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.TaskPanel;

public abstract class AbstractTaskPanelSwingWorker extends SwingWorker<Void, String> implements ActionListener
{
	protected final MainController	mainController;
	protected final MainFrame		mainFrame;
	protected final AbstractAction	action;
	protected final ActionEvent		actionEvent;
	protected final TaskPanel		taskPanel;

	public AbstractTaskPanelSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
	{
		this.mainController = MainController.getInstance();
		this.mainFrame = mainFrame;
		this.action = action;
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

	public void removeTaskPanel()
	{
		mainFrame.removeTaskPanel(taskPanel);
	}

	protected abstract void work() throws Exception;

	@Override
	protected final Void doInBackground() throws Exception
	{
		mainFrame.addTaskPanel(taskPanel);
		action.setEnabled(false);
		work();
		return null;
	}

	protected final void process(List<String> statuses)
	{
		// executed in AWTEventQueue
		taskPanel.setStatus(actionEvent.getActionCommand() + ": " + statuses.get(statuses.size() - 1));
	}

	@Override
	protected final void done()
	{
		int secs = 5;
		// executed in AWTEventQueue
		try
		{
			get();
			taskPanel.setTaskFinished(true, actionEvent.getActionCommand() + " successfull!");
			secs = 0;
		}
		catch (CancellationException e)
		{
			taskPanel.setTaskFinished(false, actionEvent.getActionCommand() + " cancelled!");
			secs = 2;
		}
		catch (ExecutionException e)
		{
			e.printStackTrace();
			taskPanel.setTaskFinished(false, actionEvent.getActionCommand() + " failed: " + e.getCause().getClass().getSimpleName() + ": "
					+ e.getCause().getMessage().replaceAll("\n.*", ""));
			// only take first line of message (replaceAll)
		}
		catch (InterruptedException e)
		{
			taskPanel.setTaskFinished(false, actionEvent.getActionCommand() + " interrupted!");
		}
		finally
		{
			final int finalSecs = secs;

			action.setEnabled(true);
			mainController.getScheduledEventDispatcher().submit(new Runnable()
			{
				@Override
				public void run()
				{
					for (int i = finalSecs; i > 0; i--)
					{
						taskPanel.setCancelText(i + "");
						try
						{
							Thread.sleep(1000);
						}
						catch (InterruptedException ignore)
						{}
					}
					removeTaskPanel();
				}
			});
		}
	}

	@Override
	public final void actionPerformed(ActionEvent e)
	{
		this.cancel(true);
	}

}