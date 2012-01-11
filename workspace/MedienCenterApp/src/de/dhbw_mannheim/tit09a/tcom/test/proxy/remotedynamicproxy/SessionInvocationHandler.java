package de.dhbw_mannheim.tit09a.tcom.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

public class SessionInvocationHandler implements InvocationHandler
{
    private static List<String> methodsExcludedFromSessionValidation = new Vector<String>();
    {
	methodsExcludedFromSessionValidation.add("addUser");
    }

    private Controller controller;

    public SessionInvocationHandler(Controller controller)
    {
	this.controller = controller;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException
    {
	System.out.println("Invoking method " + method.getName());
	try
	{
	    if (methodsExcludedFromSessionValidation.contains(method.getName()))
	    {
		return method.invoke(this.controller, args);
	    }
	    if (validateSession(args[0]))
	    {
		return method.invoke(this.controller, args);
	    }
	}
	catch (IllegalArgumentException e)
	{
	    e.printStackTrace();
	}
	catch (InvocationTargetException e)
	{
	    e.printStackTrace();
	}
	return null;
    }

    private boolean validateSession(Object sessionId) throws IllegalAccessException
    {
	if (sessionId == null)
	{
	    throw new IllegalAccessException("Illegal session ID '" + sessionId + "': null ");
	}
	else if (!(sessionId instanceof String))
	{
	    throw new IllegalAccessException("Illegal session ID '" + sessionId + "': no String ");
	}
	else if (sessionId.equals("max"))
	{
	    System.out.println("Session ID valid!");
	    return true;
	}
	else
	{
	    throw new IllegalAccessException("Illegal session ID '" + sessionId + "': not known ");
	}
    }

}
