package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.io.Serializable;

public class FileInfo implements Serializable
{
    private static final long serialVersionUID = -8161282994523577890L;
    
    private final String filename;
    private final long fileSize;
    private final boolean isDir;
    
    public FileInfo(String filename, long fileSize, boolean isDir)
    {
	this.filename = filename;
	this.fileSize = fileSize;
	this.isDir = isDir;
    }
    
    public String getFilename()
    {
	return filename;
    }
    
    public long getFileSize()
    {
	return fileSize;
    }
    
    public boolean isDir()
    {
	return isDir;
    }
    
    public String toString()
    {
	return String.format("FileInfo[%s,%d,%s]", getFilename(), getFileSize(), isDir());
    }
}
