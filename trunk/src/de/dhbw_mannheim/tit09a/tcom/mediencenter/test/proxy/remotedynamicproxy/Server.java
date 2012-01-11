package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.net.MalformedURLException;
import java.rmi.Naming;
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
	    RemoteServer.setLog(System.out);
	    
	    // Create the FileController Proxy for session validation
	    FileController fc = new FileControllerImpl();
	    InvocationHandler h = new SessionInvocationHandler(fc);
	    FileController sessionProxyFC = (FileController) AbstractController.getProxy(fc, h);
	    // Export the previous Proxy to be a remote Proxy (Dynamic Proxy)
	    FileController remoteSessionProxyFC = (FileController) UnicastRemoteObject
		    .exportObject(sessionProxyFC, 0);
	    
	    // same for LoginController
	    LoginController lc = new LoginControllerImpl();
	    h = new SessionInvocationHandler(lc);
	    LoginController sessionProxyLC = (LoginController) AbstractController.getProxy(lc, h);
	    LoginController remoteSessionProxyLC = (LoginController) UnicastRemoteObject
		    .exportObject(sessionProxyLC, 0);


	    // Registrate the Service at the RMI registry
	    LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	    Registry registry = LocateRegistry.getRegistry();
	    
	    // syntax: rmi://host:port/name
	    //Naming.rebind("rmi://localhost:1099/filecontroller", remoteSessionProxyFC);
	    registry.rebind(FileController.class.getSimpleName(), remoteSessionProxyFC);
	    System.out.println("FileController angemeldet");
	    
	    Naming.rebind(LoginController.class.getSimpleName(), remoteSessionProxyLC);
	    System.out.println("LoginController angemeldet");
	    // Alternative without URL path
	    // Registry registry = LocateRegistry.getRegistry();
	    // registry.rebind("fc", remoteSessionProxyFC);
	    
	}
	catch (RemoteException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (MalformedURLException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

    }
}
