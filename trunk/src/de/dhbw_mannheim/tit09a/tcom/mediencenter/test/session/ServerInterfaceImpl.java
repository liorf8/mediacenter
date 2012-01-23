package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.session;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class ServerInterfaceImpl implements ServerInterface, Serializable
{
    private static final long serialVersionUID = 1L;
    
    // this is where all user sessions are stored
    private List<SessionInterface> userSessions = new ArrayList<SessionInterface>();

    @Override
    public SessionInterface login(String user, ClientCallbackInterface clientCallback) throws RemoteException
    {
	System.out.println("login. user=" + user);
	clientCallback.callback("Login is in progress ...");
	System.out.flush();
	Session session = new Session(user, this);
	userSessions.add(session);
	clientCallback.callback("Session is created ... Now " + userSessions.size()
		+ " users are online ...");
	System.out.println("Session created for user " + user + ". Now " + userSessions.size()
		+ " users are online ...");
	return session;
    }

    // if a session get's unreferenced, the session is removed from the list
    void removeUserSession(Session userSession)
    {
	userSessions.remove(userSession);
	System.out.println("Removed user " + userSession.getUsername() + " from sessionlist. "
		+ userSessions.size() + " user are online.");
    }

}