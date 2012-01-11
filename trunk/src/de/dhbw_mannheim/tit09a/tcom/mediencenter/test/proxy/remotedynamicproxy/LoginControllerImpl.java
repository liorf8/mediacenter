package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.rmi.RemoteException;

public class LoginControllerImpl implements LoginController
{

    @Override
    public void addUser(String login, String password) throws RemoteException
    {
	System.out.printf("User '%s' with pw '%s' added!%n", login, password);
    }

}
