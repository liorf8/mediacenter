package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SessionInterface extends Remote
{
    public void doSomething() throws RemoteException;
}