package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class PathFileInfo implements Serializable, FileInfo
{
	private static final long	serialVersionUID	= -8161282994523577890L;

	private final Path			path;
	private final String		contentType;
	private final boolean		isDir;
	private final long			fileSize;
	private final long			lastModified;
	private final long			infoTime;
	private final boolean		isModifiable;

	public PathFileInfo(Path path, String contentType, boolean isDir, long fileSize, long lastModified, boolean isModifiable)
	{
		this.path = path;
		this.contentType = contentType;
		this.isDir = isDir;
		this.fileSize = fileSize;
		this.lastModified = lastModified;
		this.infoTime = System.currentTimeMillis();
		this.isModifiable = isModifiable;
	}

	public PathFileInfo(Path relativePath, String contentType, BasicFileAttributes attrs, boolean isModifiable)
	{
		this(relativePath, contentType, attrs.isDirectory(), attrs.size(), attrs.lastModifiedTime().toMillis(), isModifiable);
	}

	public PathFileInfo(Path absolutePath, Path parentPathToOmit, String contentType, BasicFileAttributes attrs, boolean isModifiable)
	{
		this(parentPathToOmit.relativize(absolutePath), contentType, attrs.isDirectory(), attrs.size(), attrs.lastModifiedTime().toMillis(),
				isModifiable);
	}

	public String getPath()
	{
		return path.toString();
	}

	public String getName()
	{
		return path.getFileName().toString();
	}

	public String getContentType()
	{
		return contentType;
	}

	public boolean isDir()
	{
		return isDir;
	}

	public long getSize()
	{
		return fileSize;
	}

	public long getLastModified()
	{
		return lastModified;
	}

	public long getInfoTime()
	{
		return infoTime;
	}

	public boolean isModifiable()
	{
		return isModifiable;
	}

	public String toString()
	{
		return String.format("FileInfo[%s,%s,%s,%d,%d,%d,%s]", path, contentType, isDir, fileSize, lastModified, infoTime, isModifiable);
	}
}
