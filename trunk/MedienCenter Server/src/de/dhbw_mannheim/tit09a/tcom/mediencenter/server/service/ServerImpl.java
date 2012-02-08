package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions.ServerException;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.Simon;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Server.class })
public class ServerImpl implements Server
{
    // --------------------------------------------------------------------------------
    // -- Static Variable(s) ----------------------------------------------------------
    // --------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private static final long serialVersionUID = 1L;
    
    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    // this is where all user sessions are stored
    private List<SessionImpl> userSessions = new ArrayList<SessionImpl>();

    // --------------------------------------------------------------------------------
    // -- Private/Package Method(s) ---------------------------------------------------
    // --------------------------------------------------------------------------------
    /**
     * If a session get's unreferenced, the session is removed from the list. This Method is called by the
     * unreferenced() Method in Session.
     * 
     * @param userSession
     *            The user session to remove.
     * @return True if the Session existed.
     */
    boolean removeUserSession(SessionImpl userSession)
    {
	boolean existed = userSessions.remove(userSession);
	ServerMain.serverLogger.info("Removed user " + userSession.getUser()
		+ " from sessionlist. " + userSessions.size() + " user are online @ ");
	return existed;
    }

    // --------------------------------------------------------------------------------
    // -- Public Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    @Override
    public long serverTime()
    {
	return System.currentTimeMillis();
    }

    // --------------------------------------------------------------------------------
    @Override
    public Session login(String user, String pw, ClientCallback callback)
    {
	for (SessionImpl oneSession : userSessions)
	{
	    if (oneSession.getUser().equals(user))
	    {
		// log the old client out
		boolean isSameRemote = Simon.denoteSameRemoteObjekt(callback,
			oneSession.getClientCallback());
		if (isSameRemote)
		{
		    System.out.println(Thread.currentThread()+":calling back to client");
		    callback.callback("You are already logged in on your computer. Please logout first.");
		    return null;
		}
		else
		    oneSession.getClientCallback().releaseConnection();
	    }
	}
	SessionImpl session = new SessionImpl(user, this, callback);
	userSessions.add(session);
	ServerMain.serverLogger.info("Session created for user " + user + ". Now "
		+ userSessions.size() + " users are online ... ");
	return session;
    }

    // --------------------------------------------------------------------------------
    @Override
    public void register(String user, String pw) throws IllegalArgumentException, ServerException
    {
	try
	{
	    IOUtil.ensureDoesNotExist(new File(SessionImpl.getUserRootDir(user)));
	    IOUtil.ensureValidString(user, SessionImpl.ILLEGAL_CHARS_IN_FILENAME);
	    IOUtil.ensureValidString(pw, SessionImpl.ILLEGAL_CHARS_IN_FILENAME);
	    SessionImpl.createUserDirs(user);
	}
	catch (IOException e)
	{
	    throw new ServerException(e.getMessage());
	}
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
