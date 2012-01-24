package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Service;;

public class Proxies
{
    @SuppressWarnings("unchecked")
    public static <S extends Service> S dynamicProxy(S serviceImpl, InvocationHandler h)
    {
	ClassLoader loader = serviceImpl.getClass().getClassLoader();
	Class<?>[] interfaces = serviceImpl.getClass().getInterfaces();
	return (S) Proxy.newProxyInstance(loader, interfaces, h);
    }
}
