package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.rmi.RemoteException;

public interface LoginController extends Controller
{
    public void registerUser(String login, String pw) throws RemoteException;

    public String login(String login, String pw) throws RemoteException;

    public void unregisterUser(String sessionId, String login) throws RemoteException,
	    IllegalAccessException;
}
