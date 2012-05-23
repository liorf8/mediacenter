package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public abstract class AbstractSwingWorkerAction<T, V> extends ParentAbstractAction
{
	private static final long	serialVersionUID	= 5640355678917572814L;
	
	private AbstractTaskPanelSwingWorker worker;

	public AbstractSwingWorkerAction(MainFrame mainFrame, String actionName, Icon icon)
	{
		super(mainFrame, actionName, icon);
		putValue(ACTION_COMMAND_KEY, actionName);
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e)
	{
		if(worker != null)
			worker.removeTaskPanel();
		worker = buildSwingWorker(mainFrame, this, e);
		worker.execute();
	}

	protected abstract AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e);

}
