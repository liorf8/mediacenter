package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.fileinfotree;

import java.nio.file.FileSystemException;
import java.util.List;

import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class FileTreeWillExpandListener implements TreeWillExpandListener
{

	@Override
	public void treeWillCollapse(TreeExpansionEvent e)
	{
		MainController.LOGGER.debug("source={}, path={}", e.getSource(), e.getPath());
		((DefaultMutableTreeNode) (e.getPath().getLastPathComponent())).removeAllChildren();
		((DefaultMutableTreeNode) (e.getPath().getLastPathComponent())).add(new DefaultMutableTreeNode(null));
	}

	@Override
	public void treeWillExpand(TreeExpansionEvent e)
	{
		MainController.LOGGER.debug("source={}, path={}", e.getSource(), e.getPath());
		expandNode((FileInfoTree) e.getSource(), (DefaultMutableTreeNode) (e.getPath().getLastPathComponent()));
	}
	

	// --------------------------------------------------------------------------------
	private void expandNode(FileInfoTree tree, DefaultMutableTreeNode d)
	{
		MainController.LOGGER.debug("FileInfoTree={}, DefaultMutableTreeNode={}", tree, d);
		String dirPath = ((FileInfo) d.getUserObject()).getPath();
		try
		{
			List<FileInfo> infos = MainController.getInstance().getServerConnection().getSession().listFileInfos(dirPath);
			MainController.LOGGER.trace("expanding: Node={}, List<FileInfo>={}", d, infos);
			tree.expandPath(d, infos);
		}
		catch (FileSystemException | ServerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
