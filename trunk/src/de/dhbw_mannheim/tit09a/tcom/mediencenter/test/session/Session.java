package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.session;

import java.io.Serializable;

public class Session implements SessionInterface, Serializable
{
    private static final long serialVersionUID = 3783275049171387140L;
    
    private final String user;
    private final ServerInterfaceImpl server;

    public Session(String user, ServerInterfaceImpl server)
    {
	this.user = user;
	this.server = server;
    }

    @Override
    public void doSomething()
    {
	System.out.println("User " + user + " does something ...");
    }

    public String getUsername()
    {
	return user;
    }

}