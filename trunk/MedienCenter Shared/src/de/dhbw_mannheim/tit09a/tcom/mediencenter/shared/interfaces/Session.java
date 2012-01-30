package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public interface Session
{
    public void changeAttr(String key, String newValue);

    public void deleteFile(String uri) throws IOException;

    public void renameFile(String uri, String newName) throws IOException;

    public void copyFile(String srcURI, String destURI, boolean replace) throws IOException;

    public void mkDir(String uri) throws IOException;

    public FileInfo[] listFiles(String dirURI) throws IOException;
    
    public int openFileChannel(String destURI, long fileSize);
}
