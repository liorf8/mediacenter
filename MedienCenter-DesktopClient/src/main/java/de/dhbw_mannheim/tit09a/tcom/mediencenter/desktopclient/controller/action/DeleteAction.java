package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class DeleteAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 8818453510509043691L;

	public DeleteAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setLargeIcon(MediaUtil.PATH_IMGS_16x16 + "Delete.png");
		setActionCommand("Delete");
		setShortDescription("Delete a file or a directory");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new DeleteWorker(mainFrame, action, e);
	}

	private class DeleteWorker extends AbstractTaskPanelSwingWorker
	{
		public DeleteWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
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

			String message;
			String title;
			if (selectedFileInfo.isDir())
			{
				message = "Do you really want to delete the entire directory " + selectedFileInfo.getName() + "?";
				title = "Delete directory " + selectedFileInfo.getName() + "?";
			}
			else
			{
				message = "Do you really want to delete " + selectedFileInfo.getName() + "?";
				title = "Delete " + selectedFileInfo.getName() + "?";
			}
			publish("Waiting for user input");
			int answer = JOptionPane.showConfirmDialog(mainFrame, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (answer == JOptionPane.YES_OPTION)
			{
				publish("Deleting " + selectedFileInfo.getName());
				mainController.getServerConnection().getSession().deleteFile(selectedFileInfo.getPath(), true);
				
				// reload trees
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
