package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetree.example.explorer;

import java.awt.Component;
import java.io.File;

import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

class FileTreeRenderer extends DefaultTreeCellRenderer
{

    private static final long serialVersionUID = -704155936579496781L;
    private FileSystemView fsv = FileSystemView.getFileSystemView();

    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel,
	    boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
	super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
	Object user = ((DefaultMutableTreeNode) value).getUserObject();
	if (user instanceof File)
	{
	    File f = (File) user;
	    //String name = f.getName();
	    setIcon(fsv.getSystemIcon(f));
	    setText(fsv.getSystemDisplayName(f));
	}
	return this;
    }
}