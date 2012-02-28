package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remoteproxy.rmisslexample;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Hello extends Remote
{
    String sayHello() throws RemoteException;
}
