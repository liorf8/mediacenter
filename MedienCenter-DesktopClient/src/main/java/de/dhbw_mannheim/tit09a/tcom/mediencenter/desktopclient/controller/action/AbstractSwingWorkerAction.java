package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public abstract class AbstractSwingWorkerAction extends ParentAbstractAction
{
	public AbstractSwingWorkerAction(MainFrame mainFrame)
	{
		super(mainFrame);
	}

	private static final long				serialVersionUID	= 5640355678917572814L;

	private AbstractTaskPanelSwingWorker	worker;

	@Override
	public synchronized void actionPerformed(ActionEvent e)
	{
		if (worker != null)
		{
			// TODO: think if just should be canceled, or just remove the taskPanel
			worker.cancel(true);
			worker.removeTaskPanel();
		}

		worker = buildSwingWorker(mainFrame, this, e);
		worker.execute();
	}

	protected abstract AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e);

}
