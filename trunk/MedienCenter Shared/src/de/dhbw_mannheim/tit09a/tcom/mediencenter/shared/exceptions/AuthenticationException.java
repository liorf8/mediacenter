package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.exceptions;

public class AuthenticationException extends IllegalArgumentException
{
	private static final long	serialVersionUID	= 1888419715732108217L;
	
	public AuthenticationException()
	{
		// TODO Auto-generated constructor stub
	}

	public AuthenticationException(String s)
	{
		super(s);
		// TODO Auto-generated constructor stub
	}

	public AuthenticationException(Throwable cause)
	{
		super(cause);
		// TODO Auto-generated constructor stub
	}

	public AuthenticationException(String message, Throwable cause)
	{
		super(message, cause);
		// TODO Auto-generated constructor stub
	}
	
	public AuthenticationException(String login, String pw)
	{
		super("login="+login+"; pw=" +pw+";");
	}
}
