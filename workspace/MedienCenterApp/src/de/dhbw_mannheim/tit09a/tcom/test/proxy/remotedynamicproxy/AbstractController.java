package de.dhbw_mannheim.tit09a.tcom.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract class AbstractController
{
    public static Controller getProxy(Controller c, InvocationHandler h)
    {
	ClassLoader loader = c.getClass().getClassLoader();
	Class<?>[] interfaces = c.getClass().getInterfaces();
	return (Controller) Proxy.newProxyInstance(loader, interfaces, h);
    }
}
