package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.fileinfotree;

import java.nio.file.FileSystemException;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.view.FileInfoTreeRenderer;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.PathFileInfo;

public class FileInfoTree extends JTree
{
	private static final long	serialVersionUID	= 1L;

	public FileInfoTree()
	{
		// Home Verzeichnis feststellen
		FileInfo userRoot = new PathFileInfo(MainController.getInstance().getSimonConnection().getSession().getLogin(), null, true, 0L, 0L, false);

		// Wurzelelement erstellen
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(userRoot);

		// Model erstellen mit root als Wurzelelement
		DefaultTreeModel model = new DefaultTreeModel(root);
		setModel(model);
		// setShowsRootHandles(false);
		// setRootVisible(false);

		// TreeCellRenderer setzen.
		setCellRenderer(new FileInfoTreeRenderer());

		// Listener hinzufügen
		addTreeWillExpandListener(new FileTreeWillExpandListener());

		// Wurzel aufklappen
		try
		{
			expandPath(root, MainController.getInstance().getSimonConnection().getSession().listFileInfos(""));
		}
		catch (FileSystemException | ServerException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// und so ...
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * Erweitert einen Pfad im Tree.
	 * 
	 * @param d
	 *            Den Parent Knoten
	 */
	public void expandPath(final DefaultMutableTreeNode d, List<FileInfo> fileInfos)
	{
		System.out.println("expandPath(): expanding: " + d);
		d.removeAllChildren();
		System.out.println("expandPath(): Children: " + fileInfos);

		DefaultMutableTreeNode tempNode = null;
		for (FileInfo oneFi : fileInfos)
		{
			tempNode = new DefaultMutableTreeNode(oneFi);
			if (oneFi.isDir())
				tempNode.add(new DefaultMutableTreeNode(null));
			d.add(tempNode);
		}
		// only reload the node
		reload(d);
	}

	private void reload(DefaultMutableTreeNode d)
	{
		// reload the whole tree
		if (d == null)
			((DefaultTreeModel) getModel()).reload();
		// reload a node
		else
			((DefaultTreeModel) getModel()).reload(d);
	}
}
