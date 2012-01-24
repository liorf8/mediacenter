package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;

public class Server
{
    public static void main(String[] args)
    {
	try
	{
	    RemoteServer.setLog(System.out);
	    bindControllers();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private static void bindControllers() throws Exception
    {
	LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	Registry registry = LocateRegistry.getRegistry();

	// FileController
	FileController fc = new FileControllerImpl();
	// Session Security
	InvocationHandler h = new SessionInvocationHandler(fc);
	FileController sessionedFC = (FileController) ControllerUtil.getProxiedController(fc, h);
	// Export the previous Dynamic Proxy to be a Remote Dynamic Proxy
	// and register it with the registry.
	ControllerUtil.bindController(sessionedFC, registry);

	// Same for LoginController
	LoginController lc = new LoginControllerImpl();
	h = new SessionInvocationHandler(lc);
	LoginController sessionedLC = (LoginController) ControllerUtil.getProxiedController(lc, h);
	ControllerUtil.bindController(sessionedLC, registry);
    }
}