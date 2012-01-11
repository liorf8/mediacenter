package de.dhbw_mannheim.tit09a.tcom.test.filetree.myfiletree;

import java.io.File;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class FileTreeModel implements TreeModel
{
    private EventListenerList listeners = new EventListenerList();

    private final File root;

    public FileTreeModel(File root)
    {
	this.root = root;
    }

    @Override
    public Object getRoot()
    {
	//System.out.println("getRoot()");
	return root;
    }

    @Override
    public boolean isLeaf(Object node)
    {
	//System.out.printf("isLeaf( %s )%n", node);
	return !((File) node).isDirectory();
    }

    @Override
    public Object getChild(Object parent, int index)
    {
	//System.out.printf("getChild( %s, %d )%n", parent, index);
	File[] childrens = ((File) parent).listFiles();
	if(childrens == null)
	    return null;
	return childrens[index];
    }

    @Override
    public int getChildCount(Object parent)
    {
	//System.out.printf("getChildCount( %s )%n", parent);
	File[] childrens = ((File) parent).listFiles();
	if(childrens == null)
	    return 0;
	return childrens.length;

    }

    @Override
    public int getIndexOfChild(Object parent, Object child)
    {
	//System.out.printf("getIndexOfChild( %s, %s )%n", parent, child);

	File parentFile = (File) parent;
	File childFile = (File) child;
	File[] children = parentFile.listFiles();

	for (int i = 0; i < children.length; i++)
	{
	    if (children[i].equals(childFile))
	    {
		return i;
	    }
	}

	return -1;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l)
    {
	//System.out.printf("addTreeModelListener( %s )%n", l);
	listeners.add(TreeModelListener.class, l);
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l)
    {
	//System.out.printf("removeTreeModelListener( %s )%n", l);
	listeners.remove(TreeModelListener.class, l);

    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue)
    {
	//System.out.printf("valueForPathChanged( %s, %s )%n", path, newValue);
    }

}
