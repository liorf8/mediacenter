package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated.RemoteService;

public class ServiceInvocationHandler implements InvocationHandler
{
	private final RemoteService	service;

	public ServiceInvocationHandler(RemoteService service)
	{
		this.service = service;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException
	{
		Object returnObject = null;

		try
		{
			System.out.println("Invoked by client " + RemoteServer.getClientHost());
			returnObject = method.invoke(this.service, args);
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

		return returnObject;
	}
}
