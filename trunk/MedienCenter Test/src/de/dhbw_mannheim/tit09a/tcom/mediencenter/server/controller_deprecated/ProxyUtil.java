package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class ProxyUtil
{
    public static RemoteService dynamicProxyService(RemoteService s, InvocationHandler h)
    {
	ClassLoader loader = s.getClass().getClassLoader();
	Class<?>[] interfaces = s.getClass().getInterfaces();
	return (RemoteService) Proxy.newProxyInstance(loader, interfaces, h);
    }

    public static RemoteService serviceStub(RemoteService s) throws RemoteException
    {
	return (RemoteService) UnicastRemoteObject.exportObject(s, 0);
    }
    public static String bindService(RemoteService s, Registry reg)
    {
	try
	{
	    // Get the stub
	    RemoteService stub = serviceStub(s);

	    // Register the services at the RMI registry
	    String serviceName = s.getClass().getInterfaces()[0].getSimpleName();
	    reg.rebind(serviceName, stub);
	    
	    return serviceName;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return null;
    }
}
