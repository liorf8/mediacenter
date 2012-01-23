package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.session;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote
{
    public SessionInterface login(String user, ClientCallbackInterface clientCallback) throws RemoteException;
 
 }