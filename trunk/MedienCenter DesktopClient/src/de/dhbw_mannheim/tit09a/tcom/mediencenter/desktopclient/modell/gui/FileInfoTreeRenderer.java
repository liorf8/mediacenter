package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui;

import java.awt.Component;
import java.io.File;
import java.io.IOException;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class FileInfoTreeRenderer extends DefaultTreeCellRenderer
{
    private static final long serialVersionUID = -704155936579496781L;
    private static FileSystemView fsv = FileSystemView.getFileSystemView();
    private static final Icon folderIcon = fsv.getSystemIcon(new File(System.getProperty("java.home")));
    
    private String defaultText;
    
    public FileInfoTreeRenderer(String defaultText)
    {
	this.defaultText = defaultText;
    }

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
	    boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	if (value instanceof FileInfo)
	{
	    FileInfo fi = (FileInfo) value;
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
	    if (fi.getURIPath().equals("/"))
	    {
		setText(defaultText);
	    }
	    else
	    {
		setText(fi.getName());
	    }
	}
	return this;
    }
}