package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;

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
	    LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	    Registry registry = LocateRegistry.getRegistry();

	    
	    // FileController
	    FileController fc = new FileControllerImpl();
	    // Session Security
	    InvocationHandler h = new SessionInvocationHandler(fc);
	    FileController sessionedFC = (FileController) AbstractController.getProxy(fc, h);
	    // Logging
	    h = new LogInvocationHandler(sessionedFC);
	    FileController loggedSessionedFC = (FileController) AbstractController.getProxy(
		    sessionedFC, h);
	    // Export the previous Dynamic Proxy to be a Remote Dynamic Proxy
	    FileController remoteLoggedSessionedFC = (FileController) UnicastRemoteObject
		    .exportObject(loggedSessionedFC, 0);
	    // Register the services at the RMI registry
	    registry.rebind(FileController.class.getSimpleName(), remoteLoggedSessionedFC);
	    System.out.println("FileController angemeldet");

	    // LoginController
	    LoginController lc = new LoginControllerImpl();
	    // Session Security
	    h = new SessionInvocationHandler(lc);
	    LoginController sessionedLC = (LoginController) AbstractController.getProxy(lc, h);
	    // Logging
	    h = new LogInvocationHandler(sessionedLC);
	    LoginController loggedSessionedLC = (LoginController) AbstractController.getProxy(
		    sessionedLC, h);
	    // Export the previous Dynamic Proxy to be a Remote Dynamic Proxy
	    LoginController remoteLoggedSessionLC = (LoginController) UnicastRemoteObject
		    .exportObject(loggedSessionedLC, 0);
	    // Register the services at the RMI registry
	    registry.rebind(LoginController.class.getSimpleName(), remoteLoggedSessionLC);
	    System.out.println("LoginController angemeldet");
	}
	catch (RemoteException e)
	{
	    e.printStackTrace();
	}
    }
}
