package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.session;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client
{

    public static void main(String[] args)
    {

	try
	{
	    // create a callback object
	    ClientCallbackImpl clientCallbackImpl = new ClientCallbackImpl();

	    // 'lookup' the server object
	    Registry registry = LocateRegistry.getRegistry("localhost");
	    ServerInterface server;
	    server = (ServerInterface) registry.lookup("server");
	    // use the serverobject as it would exist on your local machine
	    SessionInterface session = (SessionInterface) server.login("DonaldDuck", clientCallbackImpl);

	    session.doSomething();

	    // do some more stuff
	    // ...

	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}

    }
}
