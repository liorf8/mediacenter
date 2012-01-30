package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.FileTreeTest;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public class FileInfoTreeModel implements TreeModel
{
    private EventListenerList listeners = new EventListenerList();


    @Override
    public Object getRoot()
    {
	// System.out.println("getRoot()");
	try
	{
	    return new FileInfo("", 0L, true);
	}
	catch (URISyntaxException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	};
	return null;
    }

    @Override
    public boolean isLeaf(Object node)
    {
	// System.out.printf("isLeaf( %s )%n", node);
	return !((FileInfo) node).isDir();
    }

    @Override
    public Object getChild(Object parent, int index)
    {
	// System.out.printf("getChild( %s, %d )%n", parent, index);

	try
	{
	    FileInfo[] childrens = FileTreeTest.getChildren((FileInfo) parent);
	    if (childrens == null) return null;
	    return childrens[index];
	}
	catch (IOException e)
	{
	    e.printStackTrace();

	}
	return null;
    }

    @Override
    public int getChildCount(Object parent)
    {
	// System.out.printf("getChildCount( %s )%n", parent);
	FileInfo[] childrens;
	try
	{
	    childrens = FileTreeTest.getChildren((FileInfo) parent);
	    if (childrens == null) return 0;
	    return childrens.length;
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return 0;
    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
	// System.out.printf("getIndexOfChild( %s, %s )%n", parent, child);

	try
	{
	    FileInfo parentFileInfo = (FileInfo) parent;
	    FileInfo childFileInfo = (FileInfo) child;
	    FileInfo[] children = FileTreeTest.getChildren(parentFileInfo);
	    for (int i = 0; i < children.length; i++)
	    {
		if (children[i].equals(childFileInfo))
		{
		    return i;
		}
	    }
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
	// System.out.printf("addTreeModelListener( %s )%n", l);
	listeners.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
	// System.out.printf("removeTreeModelListener( %s )%n", l);
	listeners.remove(TreeModelListener.class, l);

    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
	// System.out.printf("valueForPathChanged( %s, %s )%n", path, newValue);
    }
}