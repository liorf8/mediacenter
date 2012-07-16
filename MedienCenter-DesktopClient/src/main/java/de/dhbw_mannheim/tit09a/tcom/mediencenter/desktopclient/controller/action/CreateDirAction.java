package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.MainFrame;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class CreateDirAction extends AbstractSwingWorkerAction
{
	private static final long	serialVersionUID	= 8818453510509043691L;

	public CreateDirAction(MainFrame mainFrame)
	{
		super(mainFrame);
		setLargeIcon(MediaUtil.PATH_IMGS_16x16 + "Create Directory.png");
		//setName("Create directory");
		setActionCommand("Create directory");
		setShortDescription("Create a directory in the chosen directory");
	}

	@Override
	protected AbstractTaskPanelSwingWorker buildSwingWorker(MainFrame mainFrame, AbstractAction action, ActionEvent e)
	{
		return new CreateDirWorker(mainFrame, action, e);
	}

	private class CreateDirWorker extends AbstractTaskPanelSwingWorker
	{
		public CreateDirWorker(MainFrame mainFrame, AbstractAction action, ActionEvent actionEvent)
		{
			super(mainFrame, action, actionEvent);
		}

		@Override
		protected void work() throws Exception
		{
			DefaultMutableTreeNode selectedServerNode = mainFrame.getManageTab().getSelectedServerNode();
			DefaultMutableTreeNode	parentNode = (DefaultMutableTreeNode)selectedServerNode.getParent();
			
			FileInfo parentDir = (FileInfo) selectedServerNode.getUserObject();
			if (!parentDir.isDir())
			{
				parentDir = (FileInfo) ((DefaultMutableTreeNode) selectedServerNode.getParent()).getUserObject();
			}
			System.out.println("parentDir=" +parentDir);
			setProgress(10);

			publish("Waiting for user input");
			Object returnValue = JOptionPane.showInputDialog(mainFrame,
					"Enter a name for the new directory",
					"New directory name",
					JOptionPane.QUESTION_MESSAGE);

			if (returnValue != null)
			{
				setProgress(30);
				publish("Creating new folder " + returnValue);
				mainController.getServerConnection().getSession().createDir(parentDir.getPath(), returnValue.toString());
				
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
