package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ControllerUtil
{
    public static Controller getProxiedController(Controller c, InvocationHandler h)
    {
	ClassLoader loader = c.getClass().getClassLoader();
	Class<?>[] interfaces = c.getClass().getInterfaces();
	return (Controller) Proxy.newProxyInstance(loader, interfaces, h);
    }

    public static String bindController(Controller c, Registry reg)
    {
	try
	{
	    // Export the Controller to be a Remote Dynamic Proxy
	    Controller remoteDynamicProxy = (Controller) UnicastRemoteObject.exportObject(c, 0);

	    // Register the services at the RMI registry
	    String controllerName = c.getClass().getInterfaces()[0].getSimpleName();
	    reg.rebind(controllerName, remoteDynamicProxy);
	    return controllerName;
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	return null;
    }
}
