package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.rmi.RemoteException;

public interface ClientCallback extends RemoteService
{
	public void callback(String text) throws RemoteException;
}