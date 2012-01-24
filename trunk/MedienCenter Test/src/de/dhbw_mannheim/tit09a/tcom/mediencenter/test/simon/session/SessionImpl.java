package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.session;

import java.io.Serializable;

import de.root1.simon.SimonUnreferenced;
import de.root1.simon.annotation.SimonRemote;

@SimonRemote(value = { Session.class })
public class SessionImpl implements Session, SimonUnreferenced, Serializable
{
    private static final long serialVersionUID = -2367173857266429637L;
    
    private final String user;
    private final LoginServiceImpl server;

    public SessionImpl(String user, LoginServiceImpl server)
    {
	this.user = user;
	this.server = server;
    }

    @Override
    public void doSomething()
    {
	System.out.println(Thread.currentThread() +": User " + user + " does something ...");
    }

    @Override
    public void unreferenced()
    {
	System.out.println(Thread.currentThread() +": Unreferenced: " + user + "@" + this);
	server.removeUserSession(this);
    }

    public String getUsername()
    {
	return user;
    }

}