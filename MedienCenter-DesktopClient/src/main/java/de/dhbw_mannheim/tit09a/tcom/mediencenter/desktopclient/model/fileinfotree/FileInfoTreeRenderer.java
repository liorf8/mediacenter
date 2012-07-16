package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.model.fileinfotree;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller.MainController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.MediaUtil;

public class FileInfoTreeRenderer extends DefaultTreeCellRenderer
{
	private static final long		serialVersionUID	= 1L;
	private static FileSystemView	fsv					= FileSystemView.getFileSystemView();
	private static final Icon		ICO_FOLDER			= fsv.getSystemIcon(new File(System.getProperty("java.home")));

	private String					rootFolderName;
	private MainController			mainController;

	public FileInfoTreeRenderer(String rootFolderName)
	{
		this.rootFolderName = rootFolderName;
		this.mainController = MainController.getInstance();
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus)
	{
		super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
		Object userObj = ((DefaultMutableTreeNode) value).getUserObject();
		if (userObj instanceof FileInfo)
		{
			FileInfo fi = (FileInfo) userObj;
			try
			{
				setText(fi.getName());
				if (!fi.isDir())
				{
					File tempFile = File.createTempFile("tmp", fi.getName());
					setIcon(fsv.getSystemIcon(tempFile));
					tempFile.delete();
				}
				else
				{
					if (fi.getPath().equals(""))
					{
						setText(rootFolderName);
						setIcon(mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "TreeView.png"));
					}
					else if (fi.getPath().equals("Music"))
					{
						setIcon(mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Folder Music.png"));
					}
					else if (fi.getPath().equals("Pictures"))
					{
						setIcon(mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Folder Pictures.png"));
					}
					else if (fi.getPath().equals("Videos"))
					{
						setIcon(mainController.getImageIcon(MediaUtil.PATH_IMGS_16x16 + "Folder Videos.png"));
					}
					else
					{
						setIcon(ICO_FOLDER);
					}
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return this;
	}
}