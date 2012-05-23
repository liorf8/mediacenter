package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions;

public class KeyDoesNotExistException extends IllegalArgumentException
{
	private static final long	serialVersionUID	= 5046106495638341096L;

	public KeyDoesNotExistException()
	{
		// TODO Auto-generated constructor stub
	}

	public KeyDoesNotExistException(String key)
	{
		super(key);
		// TODO Auto-generated constructor stub
	}

	public KeyDoesNotExistException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public KeyDoesNotExistException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
