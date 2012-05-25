package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.fileinfotree;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.util.MediaUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class FileInfoTreeRenderer extends DefaultTreeCellRenderer
{
	private static final long		serialVersionUID	= 1L;
	private static FileSystemView	fsv					= FileSystemView.getFileSystemView();
	private static final Icon		ICO_FOLDER			= fsv.getSystemIcon(new File(System.getProperty("java.home")));
	private static final Icon		ICO_ROOT			= MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "TreeView.png");
	private static final Icon		ICO_FOLDER_MUSIC	= MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Folder Music.png");
	private static final Icon		ICO_FOLDER_PICTURES	= MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Folder Pictures.png");
	private static final Icon		ICO_FOLDER_VIDEOS	= MediaUtil.createImageIcon(MediaUtil.PATH_IMGS_16x16 + "Folder Videos.png");

	private String					rootFolderName;

	public FileInfoTreeRenderer(String rootFolderName)
	{
		this.rootFolderName = rootFolderName;
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
				if (!fi.isDir())
				{
					File tempFile = File.createTempFile("tmp", fi.getName());
					setIcon(fsv.getSystemIcon(tempFile));
					tempFile.delete();
				}
				else
				{
					if (fi.getPath().equals(rootFolderName))
					{
						setIcon(ICO_ROOT);
					}
					else if (fi.getPath().equals("Music"))
					{
						setIcon(ICO_FOLDER_MUSIC);
					}
					else if (fi.getPath().equals("Pictures"))
					{
						setIcon(ICO_FOLDER_PICTURES);
					}
					else if (fi.getPath().equals("Videos"))
					{
						setIcon(ICO_FOLDER_VIDEOS);
					}
					else
					{
						setIcon(ICO_FOLDER);
					}
				}
				setText(fi.getName());
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		return this;
	}
}