package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.FileInfo;

public class PathFileInfo extends FileInfo implements Serializable
{
	private static final long	serialVersionUID	= -8161282994523577890L;

	private final String		pathString;
	private final String		contentType;
	private final boolean		isDir;
	private final long			fileSize;
	private final long			lastModified;
	private final long			infoTime;
	private final boolean		isModifiable;
	private long				elapsedTime			= -1L;

	public PathFileInfo(String relativePath, String contentType, boolean isDir, long fileSize, long lastModified, boolean isModifiable)
	{
		this.infoTime = System.currentTimeMillis();

		this.pathString = relativePath;
		this.contentType = contentType;
		this.isDir = isDir;
		this.fileSize = fileSize;
		this.lastModified = lastModified;
		this.isModifiable = isModifiable;
	}

	public PathFileInfo(Path relativePath, String contentType, boolean isDir, long fileSize, long lastModified, boolean isModifiable)
	{
		this(relativePath.toString(), contentType, isDir, fileSize, lastModified, isModifiable);
	}

	public PathFileInfo(Path relativePath, String contentType, BasicFileAttributes attrs, boolean isModifiable)
	{
		this(relativePath, contentType, attrs.isDirectory(), attrs.size(), attrs.lastModifiedTime().toMillis(), isModifiable);
	}

	@Override
	public String getPath()
	{
		return pathString;
	}

	@Override
	public String getName()
	{
		return Paths.get(pathString).getFileName().toString();
	}

	@Override
	public String getContentType()
	{
		return contentType;
	}

	@Override
	public boolean isDir()
	{
		return isDir;
	}

	@Override
	public long getSize()
	{
		return fileSize;
	}

	@Override
	public long getLastModified()
	{
		return lastModified;
	}

	@Override
	public long getInfoTime()
	{
		return infoTime;
	}

	@Override
	public boolean isModifiable()
	{
		return isModifiable;
	}

	@Override
	public long getElapsedTime()
	{
		return elapsedTime;
	}

	public void setElapsedTime(long elapsedTime)
	{
		this.elapsedTime = elapsedTime;
	}
}
