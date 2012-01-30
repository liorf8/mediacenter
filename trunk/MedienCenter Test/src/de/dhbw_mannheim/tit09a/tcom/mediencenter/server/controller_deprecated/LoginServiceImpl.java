package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.lang.reflect.InvocationHandler;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.test._old.LogInvocationHandler;


public class LoginServiceImpl extends ProxyUtil implements LoginService
{
    // this is where all user sessions are stored
    private List<Session> userSessions = new ArrayList<Session>();

    @Override
    public boolean registerUser(String login, String password) throws RemoteException
    {
	System.out.println("registerUser executed!");
	return true;
    }

    @Override
    public synchronized Session login(String login, String pw, ClientCallback callback)
	    throws RemoteException, IllegalAccessException
    {
	if (callback != null) callback.callback("Login is in progress ...");

	String sessionId = LoginController.getInstance().validateSession(login, pw);
	if (sessionId != null)
	{
	    Session session = new SessionImpl(sessionId, login);
	    InvocationHandler h = new LogInvocationHandler(session);
	    session = (Session) ProxyUtil.dynamicProxyService(session, h);
	    userSessions.add(session);
	    System.out.printf("Logged in %s. Now there are %d users online.%n", login,
		    userSessions.size());
	    if (callback != null) callback.callback("Successfully logged in.");

	    return (Session) ProxyUtil.serviceStub(session);
	}
	return null;
    }

    @Override
    public synchronized boolean logout(Session session) throws RemoteException
    {
	boolean existed = userSessions.remove(session);
	System.out.printf("Logged out %s. Now there are %d users online.%n", session.getLogin(),
		userSessions.size());

	return existed;
    }

}
