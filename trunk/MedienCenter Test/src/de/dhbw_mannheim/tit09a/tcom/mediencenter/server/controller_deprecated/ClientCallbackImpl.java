package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller_deprecated;

import java.io.Serializable;

public class ClientCallbackImpl implements ClientCallback, Serializable
{
	private static final long	serialVersionUID	= -8717731260142091920L;

	@Override
	public void callback(String text)
	{
		System.out.println("This message was received from the server: " + text);
	}
}