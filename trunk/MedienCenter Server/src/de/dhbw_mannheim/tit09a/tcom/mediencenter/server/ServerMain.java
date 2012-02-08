package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service.ServerImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service.SessionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

public class ServerMain
{
    public static Logger serverLogger = Logger.getLogger("ServerMain");
    public static Logger invocationLogger = Logger.getLogger("de.root1.simon.InvokeLogger");
    public static Registry registry;

    public static void main(String[] args)
    {
	try
	{
	    serverLogger.setLevel(Level.ALL);
	    serverLogger.addHandler(new FileHandler("mediencenter.server.log", 1024 * 1024 * 1024,
		    1, true));
	    invocationLogger.setLevel(Level.ALL);
	    invocationLogger.addHandler(new FileHandler("mediencenter.invocation.log", 1024 * 1024 * 1024,
		    1, true));

	    serverLogger.info("Starting Server ...");
	    startServer();
	    serverLogger.info("Server up and running!");
	}
	catch (Exception e)
	{
	    serverLogger.severe(e.toString());
	    e.printStackTrace();
	}
    }

    private static void startServer() throws UnknownHostException, IOException,
	    NameBindingException
    {
	IOUtil.mkDir(new File(SessionImpl.USERS_ROOT_DIR));
	// create the serverobject
	Server loginService = new ServerImpl();

	// create the server's registry ...
	registry = Simon.createRegistry(Server.REGISTRY_PORT);

	// ... where we can bind the serverobject to
	registry.bind(Server.BIND_NAME, loginService);

    }

    public static void shutdownServer(boolean now)
    {
	// some mechanism to shutdown the server should be placed here
	// this should include the following command:
	registry.unbind("server");
	registry.stop();
    }
}
