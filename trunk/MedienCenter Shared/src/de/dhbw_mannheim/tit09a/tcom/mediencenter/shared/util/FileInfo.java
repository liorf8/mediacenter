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
    private final long fileSize;
    private final boolean isDir;

    public FileInfo(String uri, long fileSize, boolean isDir) throws URISyntaxException
    {
	if (!uri.startsWith("/")) uri = "/" + uri;
	this.uriPath = new URI("file", null, uri, null).getPath();
	this.fileSize = fileSize;
	this.isDir = isDir;
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

	this.fileSize = file.length();
	this.isDir = file.isDirectory();
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

    public long getSize()
    {
	return fileSize;
    }

    public boolean isDir()
    {
	return isDir;
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
