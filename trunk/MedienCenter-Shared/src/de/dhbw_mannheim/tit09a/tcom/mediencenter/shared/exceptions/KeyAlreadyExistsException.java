package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions;

public class KeyAlreadyExistsException extends IllegalArgumentException
{
	private static final long	serialVersionUID	= 6744793488705836816L;

	public KeyAlreadyExistsException()
	{
		// TODO Auto-generated constructor stub
	}

	public KeyAlreadyExistsException(String key)
	{
		super(key);
		// TODO Auto-generated constructor stub
	}

	public KeyAlreadyExistsException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public KeyAlreadyExistsException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

}
