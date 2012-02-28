package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.session;

import java.util.ArrayList;
import java.util.List;

import de.root1.simon.annotation.SimonRemote;

// mark this class as a remote class and export all methods known in ServerInterface
@SimonRemote(value = { LoginService.class })
public class LoginServiceImpl implements LoginService
{
	// private static final long serialVersionUID = 1L;

	// this is where all user sessions are stored
	private List<Session>	userSessions	= new ArrayList<Session>();

	@Override
	public Session login(String user, ClientCallback clientCallback)
	{
		System.out.println(Thread.currentThread() + ": login. user=" + user);
		clientCallback.callback("Login is in progress ...");
		System.out.flush();
		Session session = new SessionImpl(user, this);
		userSessions.add(session);
		clientCallback.callback(Thread.currentThread() + ": Session is created ... Now " + userSessions.size() + " users are online ...");
		System.out.println(Thread.currentThread() + ": Session created for user " + user + ". Now " + userSessions.size() + " users are online ...");
		return session;
	}

	// if a session get's unreferenced, the session is removed from the list
	boolean removeUserSession(SessionImpl userSession)
	{
		boolean existed = userSessions.remove(userSession);
		System.out.println(Thread.currentThread() + ": Removed user " + userSession.getUsername() + " from sessionlist. " + userSessions.size()
				+ " user are online.");
		return existed;
	}
}
