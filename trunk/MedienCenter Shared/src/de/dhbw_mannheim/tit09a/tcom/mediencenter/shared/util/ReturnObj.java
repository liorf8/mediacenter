package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.io.Serializable;

public class ReturnObj<T> implements Serializable
{
	private static final long	serialVersionUID		= 199380435984634808L;

	// http://kb.globalscape.com/KnowledgebaseArticle10141.aspx
	public static final short	SUCCESS					= 200;
	public static final short	SUCCESS_NOT_REPLACED	= 298;
	public static final short	SUCCESS_REPLACED		= 299;

	public static final short	BAD_REQUEST				= 400;
	public static final short	ACCESS_DENIED			= 401;
	public static final short	NOT_FOUND				= 404;
	public static final short	CONFLICT				= 409;					// already exists f.i.

	public static final short	INTERNAL_SERVER_ERROR	= 500;
	public static final short	SERVICE_UNAVAILABLE		= 503;

	private final T				returnObject;
	private final short			returnCode;
	private final String		returnMessage;

	public ReturnObj()
	{
		this(null, ReturnObj.SUCCESS, null);
	}

	public ReturnObj(T returnObject)
	{
		this(returnObject, ReturnObj.SUCCESS, null);
	}

	public ReturnObj(short returnCode)
	{
		this(null, returnCode, null);
	}

	public ReturnObj(T returnObject, short returnCode)
	{
		this(returnObject, returnCode, null);
	}

	public ReturnObj(short returnCode, String message)
	{
		this(null, returnCode, message);
	}

	public ReturnObj(T returnObject, short returnCode, String returnMessage)
	{
		if (returnObject != null && !(returnObject instanceof Serializable))
			throw new IllegalArgumentException("Not instance of Serializable: " + returnObject.getClass());

		this.returnObject = returnObject;
		this.returnCode = returnCode;
		this.returnMessage = returnMessage;
	}

	public T get()
	{
		return returnObject;
	}

	public short code()
	{
		return returnCode;
	}

	public String message()
	{
		return returnMessage;
	}

	public boolean isNull()
	{
		return (returnObject == null);
	}

	public boolean wasSuccessful()
	{
		return (returnCode >= 200 && returnCode < 300);
	}

	public String codeAndMessage()
	{
		return returnCode + ": " + returnMessage;
	}

	public String toString()
	{
		return String.format("%s[%s,%d,%s]", this.getClass().getSimpleName(), returnObject, returnCode, returnMessage);
	}
}
