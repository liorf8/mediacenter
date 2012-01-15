package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

public interface FileController extends Controller
{
    public Date getServerTime() throws RemoteException;

    public void uploadFile(String sessionId, String filePath, byte[] data, boolean replace)
	    throws RemoteException, IllegalAccessException, IOException;

    public void downloadFile(String sessionId, String filePath) throws RemoteException,
	    IllegalAccessException, IOException;

    public void deleteFile(String sessionId, String filePath) throws RemoteException,
	    IllegalAccessException, IOException;

    public void renameFile(String sessionId, String filePath, String newName)
	    throws RemoteException, IllegalAccessException, IOException;

    public void copyMoveFile(String sessionId, String filePath, String copyMoveDir, boolean move)
	    throws RemoteException, IllegalAccessException, IOException;

    public void mkDir(String sessionId, String parentDir, String dir) throws RemoteException,
	    IllegalAccessException, IOException;

    public File[] listFiles(String sessionId, File dir) throws RemoteException,
	    IllegalAccessException, IOException;

    void createBasicDirs(String login) throws IOException;
}
