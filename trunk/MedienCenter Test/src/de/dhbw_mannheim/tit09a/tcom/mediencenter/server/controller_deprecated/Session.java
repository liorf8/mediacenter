package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Date;

public interface Session extends RemoteService
{
	public Date getServerTime() throws RemoteException;

	public boolean logout() throws RemoteException;

	public String getSessionId() throws RemoteException;

	public String getLogin() throws RemoteException;

	public void uploadFile(String filePath, long size, boolean replace) throws RemoteException, IOException;

	public long downloadFile(String filePath) throws RemoteException, IOException;

	public void deleteFile(String filePath) throws RemoteException, IOException;

	public void renameFile(String filePath, String newName) throws RemoteException, IOException;

	public void copyMoveFile(String filePath, String copyMoveDir, boolean move) throws RemoteException, IOException;

	public void mkDir(String parentDir, String dir) throws RemoteException, IOException;

	public File[] listFiles(File dir) throws RemoteException, IOException;

	public InputStream getStream(String sessionId, String filePath) throws RemoteException;
}
