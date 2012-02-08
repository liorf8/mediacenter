package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces;

import java.io.IOException;
import java.util.Map;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;

public interface Session
{
    public void changeAttr(String key, String newValue) throws IllegalArgumentException,
	    IOException, ServerException;

    public Map<String, String> getAttrs() throws ServerException;

    public String getAttr(String key) throws IllegalArgumentException, ServerException;

    public void copyFile(String srcURI, String destDirURI, boolean replace)
	    throws IllegalArgumentException, ServerException;

    public void deleteFile(String uri, boolean onlyIfDirIsEmpty)
	    throws IllegalArgumentException, ServerException;

    public FileInfo[] listFileInfos(String dirURI) throws IllegalArgumentException, ServerException;

    public void mkDir(String uri) throws IllegalArgumentException, ServerException;

    public int openFileChannel(String destURI, long fileSize) throws IllegalArgumentException,
	    ServerException;

    public void renameFile(String uri, String newName) throws IllegalArgumentException,
	    ServerException;

}
