package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.controller;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient.modell.connection.SimonConnection;

public class ControllerInvocationHandler implements InvocationHandler
{
	private SimonConnection	ctrl;

	public ControllerInvocationHandler(SimonConnection ctrl)
	{
		this.ctrl = ctrl;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
	{
		try
		{
			System.out.println("Invoking method " + method.getName() + "(): args=" + Arrays.toString(args));

			Object result = method.invoke(this.ctrl, args);

			System.out.println("Done.");
			return result;
		}
		catch (Throwable t)
		{
			System.out.println("invocation caused: " + t);
			throw t;
		}

	}
}
