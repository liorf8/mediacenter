package de.dhbw_mannheim.tit09a.tcom.test.proxy.dynamicproxy.example;

import java.lang.reflect.Proxy;

public class HelloDemo
{

    public static void main(String[] args)
    {
	HelloImpl helloImpl = new HelloImpl();

	ClassLoader loader = helloImpl.getClass().getClassLoader();

	Class<?>[] interfaces = new Class[] { IHello.class };
	LogHandler logHandler = new LogHandler(helloImpl);

	IHello helloProxy = (IHello) Proxy.newProxyInstance(loader, interfaces, logHandler);
	System.out.println(helloProxy.sayHello("Chris"));
    }
}