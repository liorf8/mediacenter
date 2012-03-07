package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.gui;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.PathFileInfo;

import java.nio.file.FileSystemException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import java.util.List;

public class FileInfoTreeModel_alt implements TreeModel
{
	private EventListenerList	listeners	= new EventListenerList();

	private final Session		session;

	public FileInfoTreeModel_alt(Session session)
	{
		this.session = session;
	}

	@Override
	public Object getRoot()
	{
		System.out.println("getRoot()");
		return new PathFileInfo("", "root_dir", true, 0L, System.currentTimeMillis(), true);
	}

	@Override
	public boolean isLeaf(Object node)
	{
		System.out.printf("isLeaf( %s )%n", node);
		return !((FileInfo) node).isDir();
	}

	@Override
	public Object getChild(Object parent, int index)
	{
		System.out.printf("getChild( %s, %d )%n", parent, index);
		try
		{
			String path = ((FileInfo) parent).getPath();
			List<FileInfo> children = session.listFileInfos(path);
			if (children.isEmpty())
				return null;
			return children.get(index);
		}
		catch (ServerException | IllegalArgumentException | FileSystemException e)
		{
			e.printStackTrace();
		}
		return null;

	}

	@Override
	public int getChildCount(Object parent)
	{
		System.out.printf("getChildCount( %s )%n", parent);
		try
		{
			String path = ((FileInfo) parent).getPath();
			List<FileInfo> children = session.listFileInfos(path);
			if (children.isEmpty())
				return 0;
			return children.size();
		}
		catch (ServerException | IllegalArgumentException | FileSystemException e)
		{
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int getIndexOfChild(Object parent, Object child)
	{
		System.out.printf("getIndexOfChild( %s, %s )%n", parent, child);

		try
		{
			FileInfo childFileInfo = (FileInfo) child;
			String path = ((FileInfo) parent).getPath();
			List<FileInfo> children = session.listFileInfos(path);
			for (int i = 0; i < children.size(); i++)
			{
				if (children.get(i).equals(childFileInfo))
				{
					return i;
				}
			}

		}
		catch (ServerException | IllegalArgumentException | FileSystemException e)
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
