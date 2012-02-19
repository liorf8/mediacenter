package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public class FileInfo implements Serializable
{
	private static final long	serialVersionUID	= -8161282994523577890L;

	private final String		uriPath;
	private final boolean		isDir;
	private final long			fileSize;
	private final long			lastModified;
	private final long			infoTime;
	private final boolean		isModifiable;

	public FileInfo(String uri, boolean isDir, long fileSize, long lastModified, boolean isModifiable)
	{
		this.uriPath = uri;
		this.isDir = isDir;
		this.fileSize = fileSize;
		this.lastModified = lastModified;
		this.infoTime = System.currentTimeMillis();
		this.isModifiable = isModifiable;
	}

	public FileInfo(Path relativePath, boolean isDir, long fileSize, long lastModified, boolean isModifiable)
	{
		this(MiscUtil.pathToUriString(relativePath), isDir, fileSize, lastModified, isModifiable);
	}

	public FileInfo(Path relativePath, BasicFileAttributes attrs, boolean isModifiable)
	{
		this(relativePath, attrs.isDirectory(), attrs.size(), attrs.lastModifiedTime().toMillis(), isModifiable);
	}

	public FileInfo(Path absolutPath, Path parentPathToOmit, BasicFileAttributes attrs, boolean isModifiable)
	{
		this(parentPathToOmit.relativize(absolutPath), attrs.isDirectory(), attrs.size(), attrs.lastModifiedTime().toMillis(), isModifiable);
	}

	public String getURIPath()
	{
		return uriPath;
	}

	public String getName()
	{
		String withoutSlashAtEnd = uriPath.replaceAll("/+$", "");
		return withoutSlashAtEnd.substring(withoutSlashAtEnd.lastIndexOf('/') + 1);
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
		return String.format("FileInfo[%s,%s,%d,%d,%d,%s]", uriPath, isDir, fileSize, lastModified, infoTime, isModifiable);
	}
}
