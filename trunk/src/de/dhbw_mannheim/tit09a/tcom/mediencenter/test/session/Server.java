package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.session;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Server
{

    public static void main(String[] args)
    {

	try
	{
	    // create the server's registry ...
	    LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	    Registry registry = LocateRegistry.getRegistry();

	    // create the serverobject
	    ServerInterfaceImpl serverImpl = new ServerInterfaceImpl();

	    // ... where we can bind the serverobject to
	    ServerInterface stub = (ServerInterface) UnicastRemoteObject
		    .exportObject(serverImpl, 0);
	    registry.rebind("server", stub);

	    System.out.println("Server up and running!");

	    // some mechanism to shutdown the server should be placed here
	    // this should include the following command:
	    // registry.unbind("server");
	    // registry.stop();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }
}