package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MkDirTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service.LoginServiceImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service.SessionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.LoginService;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

public class ServerMain
{
    public static Logger serverLogger = Logger.getLogger("ServerMain");
    public static final int REGISTRY_PORT = 22222;
    public static Registry registry;

    public static void main(String[] args)
    {
	try
	{
	    serverLogger.setLevel(Level.ALL);
	    serverLogger.addHandler(new FileHandler("mediencenter.server.log", 1024 * 1024 * 1024,
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
	IOUtil.executeIOTask(SessionImpl.IO_EXECUTOR, new MkDirTask(SessionImpl.USERS_ROOT_DIR));
	// create the serverobject
	LoginService loginService = new LoginServiceImpl();

	// create the server's registry ...
	registry = Simon.createRegistry(REGISTRY_PORT);

	// ... where we can bind the serverobject to
	registry.bind(LoginService.BIND_NAME, loginService);

    }
    
    public static void shutdownServer(boolean now)
    {
	// some mechanism to shutdown the server should be placed here
	// this should include the following command:
	if (now)
	    shutdownExecutorsNow();
	else
	    shutdownExecutors();
	
	registry.unbind("server");
	registry.stop();
    }

    private static void shutdownExecutors()
    {
	SessionImpl.IO_EXECUTOR.shutdown();
    }

    private static void shutdownExecutorsNow()
    {
	List<Runnable> runnablesThatWereNotExecuted = SessionImpl.IO_EXECUTOR.shutdownNow();
	serverLogger.warning("Runnables that were not executed before shutdown: "
		+ runnablesThatWereNotExecuted);
    }
}
