package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.rmi.RemoteException;

public interface LoginService extends RemoteService
{
    public boolean registerUser(String login, String pw) throws RemoteException;

    public Session login(String login, String pw) throws RemoteException, IllegalAccessException;
}
