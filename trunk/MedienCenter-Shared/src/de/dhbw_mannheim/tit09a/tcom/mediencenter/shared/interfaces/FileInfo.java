package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

public interface FileInfo
{
	public String getPath();

	public String getName();

	public boolean isDir();

	public String getContentType();

	public long getSize();

	public long getLastModified();

	public long getInfoTime();

	public boolean isModifiable();
}
