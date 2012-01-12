package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

public class SessionInvocationHandler implements InvocationHandler
{
    private static final List<Method> methodsWithoutSessionId = new Vector<Method>();
    { // static block -> is initialized with class build
	try
	{
	    methodsWithoutSessionId.add(LoginController.class.getMethod("registerUser",
		    String.class, String.class));
	    methodsWithoutSessionId.add(LoginController.class.getMethod("login", String.class,
		    String.class));
	}
	catch (SecurityException e)
	{
	    e.printStackTrace();
	}
	catch (NoSuchMethodException e)
	{
	    e.printStackTrace();
	}
    }

    private Controller controller;

    public SessionInvocationHandler(Controller controller)
    {
	this.controller = controller;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws IllegalAccessException
    {
	try
	{
	    // Methods without arguments do not need authentication
	    if (args == null)
	    {
		return method.invoke(this.controller, args);
	    }
	    // Certain Methods do not need authentication
	    if (methodsWithoutSessionId.contains(method))
	    {
		return method.invoke(this.controller, args);
	    }
	    // For all other Methods is it assumed, that args[0] is the sessionId
	    if (LoginControllerImpl.validateSession(args[0]))
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
}
