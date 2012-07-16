package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class RenameAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 8818453510509043691L;

	public RenameAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setLargeIcon(MediaUtil.PATH_IMGS_16x16 + "Rename.png");
		setActionCommand("Rename");
		setShortDescription("Rename a file or a directory");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new RenameWorker(mainFrame, action, e);
	}

	private class RenameWorker extends AbstractTaskPanelSwingWorker
	{
		public RenameWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			DefaultMutableTreeNode selectedServerNode = mainFrame.getManageTab().getSelectedServerNode();
			DefaultMutableTreeNode	parentNode = (DefaultMutableTreeNode)selectedServerNode.getParent();
			FileInfo selectedFileInfo = (FileInfo) selectedServerNode.getUserObject();
			setProgress(10);

			publish("Waiting for user input");
			Object returnValue = JOptionPane.showInputDialog(mainFrame, "Enter a new name", "Rename", JOptionPane.QUESTION_MESSAGE);

			if (returnValue != null)
			{
				setProgress(30);
				publish("Renaming " + selectedFileInfo.getName() + " to " + returnValue);
				mainController.getServerConnection().getSession().renameFile(selectedFileInfo.getPath(), returnValue.toString());
				
				if(parentNode.getPath() != null)
				{
					TreePath path = new TreePath(parentNode.getPath());
					mainFrame.getManageTab().reloadServerFileTree(path);
					mainFrame.getMediaLibraryTab().reloadFileInfoTree(path);
				}
			}
			else
			{
				this.cancel(false);
			}
		}

		@Override
		protected void finallySetActionEnabled()
		{
			// do not enable the action
		}
	}
}
