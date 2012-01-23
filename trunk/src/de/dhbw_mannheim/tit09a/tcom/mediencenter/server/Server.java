package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.lang.reflect.InvocationHandler;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.LoginService;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.LoginServiceImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.ServiceInvocationHandler;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.util.ProxyUtil;

public class Server
{
    public static void main(String[] args)
    {
	try
	{
	    RemoteServer.setLog(System.out);
	    bindServices();

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private static void bindServices() throws Exception
    {
	LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
	Registry registry = LocateRegistry.getRegistry();

	// LoginService
	LoginService s = new LoginServiceImpl();
	// Dynamic Proxy
	InvocationHandler h = new ServiceInvocationHandler(s);
	s = (LoginService) ProxyUtil.dynamicProxyService(s, h);
	
	// Export the previous Dynamic Proxy to be a Remote Dynamic Proxy
	// and register it with the registry.
	ProxyUtil.bindService(s, registry);
    }
}
