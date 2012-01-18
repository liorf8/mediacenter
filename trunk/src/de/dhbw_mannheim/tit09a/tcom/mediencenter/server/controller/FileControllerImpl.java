package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Date;

public class FileControllerImpl extends ControllerUtil implements FileController
{
    @Override
    public Date getServerTime() throws RemoteException
    {
	return new Date();
    }

    @Override
    public void deleteFile(String sessionId, String filePath) throws RemoteException,
	    IllegalAccessException, IOException
    {
	System.out.println("Deleted File '"+filePath+"'!");
    }

    @Override
    public void renameFile(String sessionId, String filePath, String newName)
	    throws RemoteException, IllegalAccessException, IOException
    {
	System.out.println("Renamed File '"+filePath+"' to "+newName+"!");
    }

    @Override
    public void copyMoveFile(String sessionId, String filePath, String copyMoveDir, boolean move)
	    throws RemoteException, IllegalAccessException, IOException
    {
	System.out.println("Copied/Moved File '"+filePath+"' to "+copyMoveDir+"!");
    }

    @Override
    public void mkDir(String sessionId, String parentDir, String dir) throws RemoteException,
	    IllegalAccessException, IOException
    {
	System.out.println("Made Directory '"+dir+" in "+parentDir+"'!");
    }

    @Override
    public File[] listFiles(String sessionId, File dir) throws RemoteException,
	    IllegalAccessException, IOException
    {
	System.out.println("Listed Files in  '"+dir+"!");
	return null;
    }

    @Override
    public void createBasicDirs(String login) throws IOException
    {
	System.out.println("Created basic directories!");
    }

    @Override
    public void uploadFile(String sessionId, String filePath, long size, boolean replace)
	    throws RemoteException, IllegalAccessException, IOException
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public long downloadFile(String sessionId, String filePath) throws RemoteException,
	    IllegalAccessException, IOException
    {
	// TODO Auto-generated method stub
	return 0;
    }
}
