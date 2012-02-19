package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions;

public class KeyDoesNotExistExpcetion extends IllegalArgumentException
{
	private static final long	serialVersionUID	= 5046106495638341096L;

	public KeyDoesNotExistExpcetion()
	{
		// TODO Auto-generated constructor stub
	}

	public KeyDoesNotExistExpcetion(String key)
	{
		super(key);
		// TODO Auto-generated constructor stub
	}

	public KeyDoesNotExistExpcetion(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public KeyDoesNotExistExpcetion(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
