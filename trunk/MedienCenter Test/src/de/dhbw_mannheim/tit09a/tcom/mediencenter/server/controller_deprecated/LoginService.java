package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.rmi.RemoteException;

public interface LoginService extends RemoteService
{
	public boolean registerUser(String login, String pw) throws RemoteException;

	public Session login(String login, String pw, ClientCallback callback) throws RemoteException, IllegalAccessException;

	public boolean logout(Session session) throws RemoteException;
}
