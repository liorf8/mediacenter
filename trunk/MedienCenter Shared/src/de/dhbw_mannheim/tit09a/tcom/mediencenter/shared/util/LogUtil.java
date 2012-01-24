package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.lang.reflect.Method;

public class LogUtil
{
    public static void logMethodEntry(Method method, Object[] args)
    {
	System.out.println(shortMethod(method, args));
    }

    public static void logMethodExit(Method method, Object returnObject)
    {
	System.out.printf("%s returning %s%n", shortMethod(method, null),
		shortReturn(method, returnObject));
    }

    public static String shortMethod(Method method, Object[] args)
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

    public static String shortReturn(Method method, Object returnObject)
    {
	return String.format("%s: %s", method.getReturnType().getName(), returnObject);
    }

}
