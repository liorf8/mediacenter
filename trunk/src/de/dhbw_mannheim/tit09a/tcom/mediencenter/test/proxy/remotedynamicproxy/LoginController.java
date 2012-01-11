package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.rmi.RemoteException;

public interface LoginController extends Controller
{
    public void addUser(String login, String password) throws RemoteException;
}
