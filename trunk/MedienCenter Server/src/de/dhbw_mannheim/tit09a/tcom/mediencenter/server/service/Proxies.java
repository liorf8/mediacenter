package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated.RemoteService;

public class Proxies
{
    public static RemoteService dynamicProxyService(Object simonRemoteImpl, InvocationHandler h)
    {
	ClassLoader loader = simonRemoteImpl.getClass().getClassLoader();
	Class<?>[] interfaces = simonRemoteImpl.getClass().getInterfaces();
	return (RemoteService) Proxy.newProxyInstance(loader, interfaces, h);
    }

}
