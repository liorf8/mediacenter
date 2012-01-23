package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.LoginService;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.Session;

public class Client
{
    public static void main(String[] args) throws RemoteException, NotBoundException
    {
	try
	{
	    // Get the RMI registry
	    Registry registry = LocateRegistry.getRegistry("localhost");
	    //Registry registry = LocateRegistry.getRegistry("192.168.2.111");
	    
	    // Get the Controllers
	    LoginService ls = (LoginService) registry.lookup(LoginService.class.getSimpleName());
	    Session session = (Session) ls.login("max", "max");
	    System.out.println(session.getServerTime());
	    System.out.println(session.getSessionId());
	    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }
}