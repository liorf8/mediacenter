package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;

public abstract class AbstractSwingWorkerAction<T, V> implements ActionListener
{
	private MainFrame	mainFrame;
	private AbstractTaskPanelSwingWorker worker;

	public AbstractSwingWorkerAction(MainFrame mainFrame)
	{
		this.mainFrame = mainFrame;
	}

	@Override
	public synchronized void actionPerformed(ActionEvent e)
	{
		if(worker != null)
			worker.removeTaskPanel();
		worker = buildSwingWorker(MainController.getInstance(), mainFrame, e);
		worker.execute();
	}

	protected abstract AbstractTaskPanelSwingWorker buildSwingWorker(MainController mainController, MainFrame mainFrame, ActionEvent e);

}
