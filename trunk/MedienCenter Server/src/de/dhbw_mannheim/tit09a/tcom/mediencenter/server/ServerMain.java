package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.IOException;
import java.net.UnknownHostException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service.LoginServiceImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.LoginService;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

public class ServerMain
{
    public static void main(String[] args)
    {
	try
	{
	    System.out.println(Thread.currentThread() + ": Starting Server ...");
	    startServer();
	    System.out.println(Thread.currentThread() + ": Server up and running!");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private static void startServer() throws UnknownHostException, IOException,
	    NameBindingException
    {
	// create the serverobject
	// LoginService
	LoginService loginService = new LoginServiceImpl();
	// Dynamic Proxy
	//InvocationHandler h = new LogInvocationHandler(loginService);
	//loginService = (LoginService) Proxies.dynamicProxy(loginService, h);

	// create the server's registry ...
	Registry registry = Simon.createRegistry(22222);

	// ... where we can bind the serverobject to
	registry.bind("server", loginService);

	// some mechanism to shutdown the server should be placed here
	// this should include the following command:
	// registry.unbind("server");
	// registry.stop();
    }
}
