package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public interface Session extends Service
{
    public long getServerTime();

    public void changeAttr(String key, String newValue);

    public void deleteFile(String filePath) throws IOException;

    public void renameFile(String filePath, String newName) throws IOException;

    public void copyMoveFile(String filePath, String targetDirPath, boolean move)
	    throws IOException;

    public void mkDir(String parentDir, String dirPath) throws IOException;

    public FileInfo[] listFiles(String dirPath) throws IOException;
}
