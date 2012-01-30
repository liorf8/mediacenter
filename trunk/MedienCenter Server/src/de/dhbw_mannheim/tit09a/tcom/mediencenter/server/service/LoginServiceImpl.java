package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.LoginService;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { LoginService.class })
public class LoginServiceImpl implements LoginService
{

    // private static final long serialVersionUID = 1L;

    // this is where all user sessions are stored
    private List<Session> userSessions = new ArrayList<Session>();

    @Override
    public long getServerTime()
    {
	return System.currentTimeMillis();
    }

    @Override
    public Session login(String user, ClientCallback clientCallback)
    {
	clientCallback.callback("Login is in progress ...");
	System.out.flush();
	Session session = new SessionImpl(user, this);
	userSessions.add(session);
	clientCallback.callback(Thread.currentThread() + ": Session is created ... Now "
		+ userSessions.size() + " users are online ...");
	ServerMain.serverLogger.fine(Thread.currentThread() + ": Session created for user " + user + ". Now "
		+ userSessions.size() + " users are online ...");
	return session;
    }

    // if a session get's unreferenced, the session is removed from the list
    boolean removeUserSession(SessionImpl userSession)
    {
	boolean existed = userSessions.remove(userSession);
	ServerMain.serverLogger.fine(Thread.currentThread() + ": Removed user " + userSession.getUser()
		+ " from sessionlist. " + userSessions.size() + " user are online.");
	return existed;
    }

    @Override
    public void register(String user, String pw) throws IllegalArgumentException, IOException
    {
	IOUtil.ensureValidString(BIND_NAME, SessionImpl.ILLEGAL_CHARS_IN_FILENAME);
	SessionImpl.createUserDirs(user);
    }

}
