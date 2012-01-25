package de.dhbw_mannheim.tit09a.tcom.mediencenter.desktopclient;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.LoginService;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Session;
import de.root1.simon.Lookup;
import de.root1.simon.exceptions.EstablishConnectionFailed;
import java.io.IOException;
import java.util.Arrays;

import de.root1.simon.Simon;
import de.root1.simon.exceptions.LookupFailedException;

public class ClientMain
{

    public static void main(String[] args) throws IOException, LookupFailedException,
	    EstablishConnectionFailed
    {

	Lookup nameLookup = null;
	LoginService server = null;
	try
	{
	    // create a callback object
	    ClientCallbackImpl clientCallbackImpl = new ClientCallbackImpl();

	    // 'lookup' the server object
	    nameLookup = Simon.createNameLookup("127.0.0.1", 22222);
	    server = (LoginService) nameLookup.lookup("server");

	    // use the serverobject as it would exist on your local machine
	    System.out.println(Thread.currentThread() + ": Logging in ...");
	    server.register("Donald Duck", "123");
	    Session mySession = server.login("Donald Duck", clientCallbackImpl);
	    System.out.println(Arrays.toString(mySession.listFiles("")));

	    // do some more stuff
	    // ...

	}
	catch (Throwable t)
	{
	    t.printStackTrace();
	}
	finally
	{
	    // and finally 'release' the serverobject to release to connection to the server
	    nameLookup.release(server);
	}
    }
}