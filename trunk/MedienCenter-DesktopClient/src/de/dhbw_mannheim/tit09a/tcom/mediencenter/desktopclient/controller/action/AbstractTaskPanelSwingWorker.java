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

public abstract class AbstractTaskPanelSwingWorker extends SwingWorker<Void, String> implements ActionListener
{
	protected final MainController	mainController;
	protected final MainFrame		mainFrame;
	protected final ActionEvent		actionEvent;
	protected final TaskPanel		taskPanel;

	public AbstractTaskPanelSwingWorker(MainController mainController, MainFrame mainFrame, ActionEvent actionEvent)
	{
		this.mainController = mainController;
		this.mainFrame = mainFrame;
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
		publish(actionEvent.getActionCommand() + "...");
		mainFrame.getLoginPanel().setActionEnabled(false);
		work();
		return null;
	}

	protected final void process(List<String> statuses)
	{
		// executed in AWTEventQueue
		taskPanel.setStatus(statuses.get(statuses.size() - 1));
	}

	@Override
	protected final void done()
	{
		int secs = 5;
		// executed in AWTEventQueue
		try
		{
			get();
			setProgress(100);
			taskPanel.setStatus(actionEvent.getActionCommand() + " successfull!");
			secs = 2;
		}
		catch (CancellationException e)
		{
			taskPanel.setTaskFailed(actionEvent.getActionCommand() + " cancelled!");
			secs = 2;
		}
		catch (ExecutionException e)
		{
			taskPanel.setTaskFailed(actionEvent.getActionCommand() + " failed: " + e.getCause().getClass().getSimpleName() + ": "
					+ e.getCause().getMessage());
		}
		catch (InterruptedException e)
		{
			taskPanel.setTaskFailed(actionEvent.getActionCommand() + " interrupted!");
		}
		finally
		{
			final int finalSecs = secs;
			mainFrame.getLoginPanel().setActionEnabled(true);
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