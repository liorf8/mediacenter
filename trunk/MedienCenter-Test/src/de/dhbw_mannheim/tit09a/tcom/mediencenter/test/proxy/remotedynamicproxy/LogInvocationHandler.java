package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.proxy.remotedynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogInvocationHandler implements InvocationHandler
{
    private final Object source;

    public LogInvocationHandler(Object source)
    {
	this.source = source;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
	logMethodEntry(method, args);
	Object returnValue = method.invoke(this.source, args);
	logMethodExit(method, returnValue);
	return returnValue;
    }

    private static void logMethodEntry(Method method, Object[] args)
    {
	System.out.println(shortMethod(method, args));
    }

    private static void logMethodExit(Method method, Object returnValue)
    {
	System.out.printf("%s returning %s%n", shortMethod(method, null),
		shortReturn(method, returnValue));
    }

    private static String shortMethod(Method method, Object[] args)
    {
	StringBuilder paramsSB = new StringBuilder();
	Class<?>[] params = method.getParameterTypes();
	if (args != null) // do not add the parameters if null
	{
	    for (int i = 0; i < params.length; i++)
	    {
		paramsSB.append(String.format("%s: %s, ", params[i].getName(), args[i]));
	    }
	    if (paramsSB.length() > 0)
	    {
		paramsSB.delete(paramsSB.length() - 2, paramsSB.length());
	    }
	}

	return String.format("%s.%s(%s)", method.getDeclaringClass().getSimpleName(),
		method.getName(), paramsSB.toString());
    }

    private static String shortReturn(Method method, Object returnValue)
    {
	return String.format("%s: %s", method.getReturnType().getName(), returnValue);
    }

}
