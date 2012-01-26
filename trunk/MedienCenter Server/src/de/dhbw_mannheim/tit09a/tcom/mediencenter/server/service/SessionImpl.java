package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import java.io.IOException;
import java.io.Serializable;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.*;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.FileInfo;
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

    // Overriding Session
    @Override
    public void changeAttr(String key, String newValue)
    {
	System.out.println(Thread.currentThread() + ": "
		+ UserController.changeAttr(this.getUser(), key, newValue));
    }

    @Override
    public void deleteFile(String filePath) throws IOException
    {
	IOController.deleteFile(this.getUser(), filePath);
    }

    @Override
    public void renameFile(String filePath, String newName) throws IOException
    {
	IOController.renameFile(this.getUser(), filePath, newName);
    }

    @Override
    public void copyFile(String srcPath, String destPath, boolean replace) throws IOException
    {
	IOController.copyFile(this.getUser(), srcPath, destPath, replace);
    }

    @Override
    public void mkDir(String parentDir, String dirName) throws IOException
    {
	IOController.mkDir(this.getUser(), parentDir, dirName);
    }

    @Override
    public FileInfo[] listFiles(String dirPath) throws IOException
    {
	return IOController.listFilenames(this.getUser(), dirPath);
    }

    public int openFileChannel(String destPath, long fileSize)
    {
	return IOController.openFileChannel(getUser(), this, destPath, fileSize);
    }
    
    // Overriding SimonUnreferenced
    @Override
    public void unreferenced()
    {
	System.out.println(Thread.currentThread() + ": Unreferenced: " + user + "@" + this);
	loginService.removeUserSession(this);
    }

    //
    public String getUser()
    {
	return user;
    }

    public String getSessionId()
    {
	return sessionId;
    }

    public String toString()
    {
	return this.getSessionId();
    }


}
