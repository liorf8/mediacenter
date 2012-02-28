package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Vector;

public class SessionInvocationHandler implements InvocationHandler
{
    private static final List<Method> methodsWithSessionId = new Vector<Method>();
    { // static block -> is initialized with class build
	try
	{
	    methodsWithSessionId.add(LoginController.class.getMethod("unregisterUser",
		    String.class, String.class));
	    methodsWithSessionId.add(FileController.class.getMethod("addFile", String.class,
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
	LogUtil.logMethodEntry(method, args);
	Object returnObject = null;
	try
	{
	    // Certain Methods need authentication via session id
	    if (methodsWithSessionId.contains(method))
	    {
		if (LoginControllerImpl.validateSession(args[0]))
		{
		    returnObject = method.invoke(this.controller, args);
		}
	    }
	    else
	    {
		// All other Methods
		returnObject = method.invoke(this.controller, args);
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
	LogUtil.logMethodExit(method, returnObject);

	return returnObject;
    }
}
