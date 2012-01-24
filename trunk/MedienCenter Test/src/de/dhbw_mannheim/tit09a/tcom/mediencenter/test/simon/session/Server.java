package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.simon.session;

import java.io.IOException;
import java.net.UnknownHostException;

import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

public class Server
{

    public static void main(String[] args) throws UnknownHostException, IOException,
	    NameBindingException
    {

	// create the serverobject
	LoginServiceImpl loginService = new LoginServiceImpl();

	// create the server's registry ...
	Registry registry = Simon.createRegistry(22222);

	// ... where we can bind the serverobject to
	registry.bind("server", loginService);

	System.out.println(Thread.currentThread() +": Server up and running!");

	// some mechanism to shutdown the server should be placed here
	// this should include the following command:
	// registry.unbind("server");
	// registry.stop();
    }

}