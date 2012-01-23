package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.lang.reflect.InvocationHandler;
import java.rmi.RemoteException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.util.ProxyUtil;

public class LoginServiceImpl extends ProxyUtil implements LoginService
{
    @Override
    public boolean registerUser(String login, String password) throws RemoteException
    {
	System.out.println("registerUser executed!");
	return true;
    }

    @Override
    public Session login(String login, String pw) throws RemoteException, IllegalAccessException
    {
	String sessionId = LoginController.getInstance().validateSession(login, pw);
	if (sessionId != null)
	{
	    Session session = new SessionImpl(sessionId, login);
	    InvocationHandler h = new ServiceInvocationHandler(session);
	    session = (Session) ProxyUtil.dynamicProxyService(session, h);
	    return (Session) ProxyUtil.serviceStub(session);
	}
	return null;
    }

}
