package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientCallbackInterface extends Remote
{
    public void callback(String text) throws RemoteException;
}