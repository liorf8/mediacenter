package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.rmi.RemoteException;

public interface LoginController extends Controller
{
    public void registerUser(String login, String pw) throws RemoteException;

    public String login(String login, String pw) throws RemoteException;

    public void logout(String sessionId) throws RemoteException, IllegalAccessException;

    public void changePW(String sessionId, String oldPW, String newPW) throws RemoteException,
	    IllegalAccessException;

    public void unregisterUser(String sessionId, String login) throws RemoteException,
	    IllegalAccessException;
}
