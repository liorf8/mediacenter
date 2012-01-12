package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.dynamicproxy.myproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class SessionHandler implements InvocationHandler
{
    private Controller controller;
    private String sessionId;

    public SessionHandler(Controller controller, String sessionId)
    {
	this.controller = controller;
	this.sessionId = sessionId;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
	if (!validateSession())
	    throw new IllegalArgumentException("Illegal session ID: " + sessionId);

	System.out.println("Invoking method " + method.getName());
	Object result = method.invoke(this.controller, args);
	System.out.println("Done.");
	System.out.println();
	return result;
    }

    private boolean validateSession()
    {
	System.out.println("Validating Session-ID '" + sessionId + "'...");
	if (sessionId.equals("pw")) return true;

	return false;

    }
}
