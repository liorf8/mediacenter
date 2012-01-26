package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class Proxies
{
    @SuppressWarnings("unchecked")
    public static <T> T dynamicProxy(T serviceImpl, InvocationHandler h)
    {
	ClassLoader loader = serviceImpl.getClass().getClassLoader();
	Class<?>[] interfaces = serviceImpl.getClass().getInterfaces();
	return (T) Proxy.newProxyInstance(loader, interfaces, h);
    }
}
