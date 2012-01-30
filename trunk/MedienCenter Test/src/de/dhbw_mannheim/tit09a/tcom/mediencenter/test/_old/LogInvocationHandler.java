package de.dhbw_mannheim.tit09a.tcom.mediencenter.test._old;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.LogUtil;

public class LogInvocationHandler implements InvocationHandler
{
    private final Object source;
    public LogInvocationHandler(Object source)
    {
	this.source = source;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException
    {
	LogUtil.logMethodEntry(method, args);
	Object returnObject = null;

	try
	{
	    System.out.println("Invoked by client " +RemoteServer.getClientHost());
	    returnObject = method.invoke(this.source, args);
	}
	catch (IllegalArgumentException e)
	{
	    e.printStackTrace();
	}
	catch (InvocationTargetException e)
	{
	    e.printStackTrace();
	}
	catch (ServerNotActiveException e)
	{
	    e.printStackTrace();
	}
	
	LogUtil.logMethodExit(method, returnObject);
	return returnObject;
    }
}
