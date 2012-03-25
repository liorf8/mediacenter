package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test;

import java.nio.file.FileSystemException;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.PathFileInfo;

public class FileInfoTree_alt extends JTree implements TreeWillExpandListener
{
	private static final long	serialVersionUID	= -2444667977301185992L;

	private Session				session;

	public FileInfoTree_alt(Session session)
	{
		this.session = session;

		// Home Verzeichnis feststellen
		FileInfo userRoot = new PathFileInfo("", null, true, 0L, 0L, false);
		// Wurzelelement erstellen
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(userRoot);
		// Model erstellen mit root als Wurzelelement
		DefaultTreeModel model = new DefaultTreeModel(root);
		setModel(model);
		setShowsRootHandles(false);
		// TreeCellRenderer setzen.
		setCellRenderer(new FileInfoTreeRenderer(session.getLogin()));
		// Wurzel aufklappen
		expandPath(root);
		// Listener hinzufügen
		addTreeWillExpandListener(this);
		// und so ...
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	/**
	 * Erweitert einen Pfad im Tree.
	 * 
	 * @param d
	 *            Den Parent Knoten
	 */
	private void expandPath(final DefaultMutableTreeNode d)
	{
		System.out.println("expanding: " + d);
		try
		{
			d.removeAllChildren();
			String path = ((FileInfo) d.getUserObject()).getPath();
			List<FileInfo> tmpFileInfos = session.listFileInfos(path);
			System.out.println("Children: " + tmpFileInfos);

			DefaultMutableTreeNode tempNode = null;
			for (FileInfo oneFi : tmpFileInfos)
			{
				tempNode = new DefaultMutableTreeNode(oneFi);
				if (oneFi.isDir())
					tempNode.add(new DefaultMutableTreeNode(null));
				d.add(tempNode);
			}
			((DefaultTreeModel) getModel()).reload(d);
		}
		catch (ServerException | FileSystemException e)
		{
			System.err.println("Path could not be expaned: " + d);
			e.printStackTrace();
		}
	}
	
	public void reload()
	{
		((DefaultTreeModel) getModel()).reload();
	}

	public FileInfo getSelectedFileInfo()
	{
		TreePath selectionPath = getSelectionPath();
		if (selectionPath == null)
			return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) selectionPath.getLastPathComponent();
		return (FileInfo) node.getUserObject();
	}

	public void treeWillCollapse(TreeExpansionEvent e)
	{
		((DefaultMutableTreeNode) (e.getPath().getLastPathComponent())).removeAllChildren();
		((DefaultMutableTreeNode) (e.getPath().getLastPathComponent())).add(new DefaultMutableTreeNode(null));
	}

	public void treeWillExpand(TreeExpansionEvent e)
	{
		expandPath((DefaultMutableTreeNode) (e.getPath().getLastPathComponent()));
	}

}
