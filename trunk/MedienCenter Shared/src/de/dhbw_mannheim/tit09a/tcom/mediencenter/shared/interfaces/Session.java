package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public interface Session
{
    public void changeAttr(String key, String newValue);

    public void deleteFile(String filePath) throws IOException;

    public void renameFile(String filePath, String newName) throws IOException;

    public void copyFile(String srcPath, String destPath, boolean replace) throws IOException;

    public void mkDir(String parentDir, String dirName) throws IOException;

    public FileInfo[] listFiles(String dirPath) throws IOException;
    
    public int openFileChannel(String destPath, long fileSize);
}
