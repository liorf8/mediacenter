package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import java.io.IOException;
import java.io.Serializable;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Session.class })
public class SessionImpl implements Session, SimonUnreferenced, Serializable
{
    private static final long serialVersionUID = 4608946221610753415L;
    private final String user;
    private final LoginServiceImpl loginService;
    private final String sessionId;
    
    SessionImpl(String user, LoginServiceImpl loginService)
    {
	this.user = user;
	this.loginService = loginService;
	this.sessionId = "default_session_id";
    }
    
    @Override
    public long getServerTime()
    {
	// TODO Auto-generated method stub
	return 0;
    }

    @Override
    public void changeAttr(String key, String newValue)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void deleteFile(String filePath) throws IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void renameFile(String filePath, String newName) throws IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void copyMoveFile(String filePath, String targetDirPath, boolean move)
	    throws IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void mkDir(String parentDir, String dirPath) throws IOException
    {
	// TODO Auto-generated method stub

    }

    @Override
    public String[] listFiles(String dirPath) throws IOException
    {
	// TODO Auto-generated method stub
	return null;
    }

    @Override
    public void unreferenced()
    {
	System.out.println(Thread.currentThread() +": Unreferenced: " + user + "@" + this);
	loginService.removeUserSession(this);
    }

    public String getUser()
    {
	return user;
    }
    
    public String getSessionId()
    {
	return sessionId;
    }
}
