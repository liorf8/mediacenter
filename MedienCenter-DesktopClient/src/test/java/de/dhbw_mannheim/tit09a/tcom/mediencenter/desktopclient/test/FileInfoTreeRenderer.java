package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.test;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

class FileInfoTreeRenderer extends DefaultTreeCellRenderer
{
	private String root;

	public FileInfoTreeRenderer(String root)
	{
		this.root = root;
	}

	private static final long		serialVersionUID	= -704155936579496781L;
	private static FileSystemView	fsv					= FileSystemView.getFileSystemView();
	private static final Icon		folderIcon			= fsv.getSystemIcon(new File(System.getProperty("java.home")));

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		Object userObj = ((DefaultMutableTreeNode) value).getUserObject();
		if (userObj instanceof FileInfo)
		{
			FileInfo fi = (FileInfo) userObj;
			try
			{
				if (!fi.isDir())
				{
					File tempFile = File.createTempFile("tmp", fi.getName());
					setIcon(fsv.getSystemIcon(tempFile));
					tempFile.delete();
				}
				else
				{
					setIcon(folderIcon);
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			if (fi.getPath().isEmpty())
			{
				setText(root);
			}
			else
			{
				setText(fi.getName());
			}
		}
		return this;
	}
}