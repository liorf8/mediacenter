package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;
import java.util.Date;


public class SessionImpl implements Session
{

    private final String sessionId;
    private final String login;

    SessionImpl(String sessionId, String login)
    {
	this.sessionId = sessionId;
	this.login = login;
    }

    @Override
    public Date getServerTime() throws RemoteException
    {
	return new Date();
    }

    @Override
    public void uploadFile(String filePath, long size, boolean replace) throws RemoteException,
	    IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public long downloadFile(String filePath) throws RemoteException, IOException
    {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void deleteFile(String filePath) throws RemoteException, IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void renameFile(String filePath, String newName) throws RemoteException, IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void copyMoveFile(String filePath, String copyMoveDir, boolean move)
	    throws RemoteException, IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void mkDir(String parentDir, String dir) throws RemoteException, IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public File[] listFiles(File dir) throws RemoteException, IOException
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public String getSessionId()
    {
	return sessionId;
    }

    @Override
    public InputStream getStream(String sessionId, String filePath) throws RemoteException
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public boolean logout() throws RemoteException
    {
	return false;
    }

    @Override
    public String getLogin()
    {
	return this.login;
    }

    // public String toString()
    // {
    // return String.format("%s[%s,%s]", this.getClass().getSimpleName(), getLogin(), getSessionId());
    // }

    public boolean equals(Object o)
    {
	try
	{
	    Session s = (Session) o;
	    if (s.getSessionId().equals(this.getSessionId()))
	    {
		return true;
	    }
	}
	catch (ClassCastException e)
	{
	    e.printStackTrace();
	    return false;
	}
	catch (RemoteException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	return super.equals(o);
    }
}
