package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingWorker;

public abstract class AbstractSwingWorkerAction<T, V> implements ActionListener
{
	private SwingWorker<T, V>	task;

	@Override
	public final void actionPerformed(ActionEvent e)
	{
		System.out.println("actionPerformed(): " +this.getClass().getSimpleName());
		cancelAction();
		synchronized (this)
		{
			task = buildSwingWorker(e);
			task.execute();
		}
	}

	public final void cancelAction()
	{
		synchronized (this)
		{
			if (task != null)
				task.cancel(true);
		}
	}

	protected abstract SwingWorker<T, V> buildSwingWorker(ActionEvent e);
}
