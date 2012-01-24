package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.io.IOException;

public interface Session extends Service
{
    public long getServerTime();

    public void changeAttr(String key, String newValue);

    public void deleteFile(String filePath) throws IOException;

    public void renameFile(String filePath, String newName) throws IOException;

    public void copyMoveFile(String filePath, String targetDirPath, boolean move)
	    throws IOException;

    public void mkDir(String parentDir, String dirPath) throws IOException;

    public String[] listFiles(String dirPath) throws IOException;
}
