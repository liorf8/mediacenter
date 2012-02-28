package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import java.nio.file.Path;
import java.util.LinkedList;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class FileInfoCache
{
	public int						size	= 64;
	private LinkedList<FileInfo>	cache	= new LinkedList<>();

	// copy on write list / vector

	public FileInfoCache(int size)
	{
		this.size = size;
	}

	public FileInfo lookup(Path path)
	{
		if (path == null)
			return null;

		synchronized (cache)
		{
			for (FileInfo oneInfo : cache)
			{
				// hit
				if (oneInfo.getPath().equals(path.toString()))
					return oneInfo;
			}
			// miss
			return null;
		}
	}

	public boolean insert(FileInfo info)
	{
		if (info == null)
			return false;

		synchronized (cache)
		{
			for (FileInfo oneInfo : cache)
			{
				if (oneInfo.getPath().equals(info.getPath()))
					return false;
			}

			cache.addFirst(info);
			if (cache.size() > size)
				cache.removeLast();

			return true;
		}
	}

	public String toString()
	{
		return FileInfoCache.class.getSimpleName() + cache.toString();
	}

}
