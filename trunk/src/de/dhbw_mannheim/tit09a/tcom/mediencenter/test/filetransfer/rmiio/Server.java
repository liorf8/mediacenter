package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer.rmiio;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.UnicastRemoteObject;

public class Server
{
    public static void main(String[] args)
    {
	try
	{
	    LocateRegistry.createRegistry(Registry.REGISTRY_PORT);

	    FileServiceImpl fileService = new FileServiceImpl();
	    FileService stub = (FileService) UnicastRemoteObject.exportObject(fileService, 0);
	    RemoteServer.setLog(System.out);

	    Registry registry = LocateRegistry.getRegistry();
	    registry.rebind("FileService", stub);

	    System.out.println("FileService angemeldet");
	}
	catch (RemoteException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
