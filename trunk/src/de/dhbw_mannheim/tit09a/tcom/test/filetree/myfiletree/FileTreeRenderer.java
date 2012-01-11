package de.dhbw_mannheim.tit09a.tcom.test.filetree.myfiletree;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;

class FileTreeRenderer extends DefaultTreeCellRenderer
{

    private static final long serialVersionUID = -704155936579496781L;
    private FileSystemView fsv = FileSystemView.getFileSystemView();

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
	    boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	if (value instanceof File)
	{
	    File f = (File) value;
	    setIcon(fsv.getSystemIcon(f));
	    setText(fsv.getSystemDisplayName(f));
	}
	return this;
    }
}