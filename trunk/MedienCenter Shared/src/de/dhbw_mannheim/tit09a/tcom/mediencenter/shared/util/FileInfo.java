package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

public class FileInfo implements Serializable
{
    private static final long serialVersionUID = -8161282994523577890L;

    private final String uriPath;
    private final boolean isDir;
    private final long fileSize;
    private final long lastModified;
    private final long infoTime;

    public FileInfo(String uri, boolean isDir, long fileSize, long lastModified)
	    throws URISyntaxException
    {
	if (!uri.startsWith("/")) uri = "/" + uri;
	this.uriPath = new URI("file", null, uri, null).getPath();
	this.isDir = isDir;
	this.fileSize = fileSize;
	this.lastModified = lastModified;
	this.infoTime = System.currentTimeMillis();

    }

    public FileInfo(File file, File parentDirToOmit)
    {
	if (parentDirToOmit == null)
	{
	    this.uriPath = file.toURI().getPath();
	}
	else
	{
	    this.uriPath = file.toURI().getPath()
		    .replaceFirst(Pattern.quote(parentDirToOmit.toURI().getPath()), "/");
	}

	this.isDir = file.isDirectory();
	this.fileSize = file.length();
	this.lastModified = file.lastModified();
	this.infoTime = System.currentTimeMillis();
    }

    public FileInfo(File file)
    {
	this(file, null);
    }

    public String getURIPath()
    {
	return uriPath;
    }

    public String toFilePath()
    {
	return uriPathToFilePath(uriPath);
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
    
    public String toString()
    {
	return String.format("FileInfo[%s,%d,%s]", getURIPath(), getSize(), isDir());
    }

    public static String uriPathToFilePath(String uriPath)
    {
	return uriPath.replace('/', File.separatorChar);
    }
}
