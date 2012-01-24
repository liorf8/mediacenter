package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.session;

import de.root1.simon.Lookup;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import java.io.IOException;

import de.root1.simon.Simon;
import de.root1.simon.exceptions.LookupFailedException;

public class Client
{

    public static void main(String[] args) throws IOException, LookupFailedException,
	    EstablishConnectionFailed
    {

	// create a callback object
	ClientCallbackImpl clientCallbackImpl = new ClientCallbackImpl();

	// 'lookup' the server object
	Lookup nameLookup = Simon.createNameLookup("127.0.0.1", 22222);
	LoginService server = (LoginService) nameLookup.lookup("server");

	// use the serverobject as it would exist on your local machine
	System.out.println(Thread.currentThread() + ": Logging in ...");
	Session mySession = server.login("Donald Duck", clientCallbackImpl);
	mySession.doSomething();

	// do some more stuff
	// ...

	// and finally 'release' the serverobject to release to connection to the server
	nameLookup.release(server);
    }
}