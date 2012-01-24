package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated.ClientCallback;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated.ClientCallbackImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated.LoginService;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated.Session;

public class Client
{
    public static void main(String[] args) throws RemoteException, NotBoundException
    {
	try
	{
	    ClientCallback callback = new ClientCallbackImpl();
	    // Get the RMI registry
	    Registry registry = LocateRegistry.getRegistry("localhost");
	    //Registry registry = LocateRegistry.getRegistry("192.168.2.111");
	    
	    // Get the Controllers
	    LoginService ls = (LoginService) registry.lookup(LoginService.class.getSimpleName());
	    Session session = (Session) ls.login("max", "max", callback);
	    System.out.println(session.getServerTime());
	    System.out.println(session.getSessionId());
	    System.out.println("Logging out ..." +session.logout());
	    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }
}