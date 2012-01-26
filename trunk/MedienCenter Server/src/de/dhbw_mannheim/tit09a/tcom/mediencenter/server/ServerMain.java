package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.IOController;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.controller.tasks.MkDirTask;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.service.LoginServiceImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.LoginService;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.exceptions.NameBindingException;

public class ServerMain
{
    private static Logger logger;
    private static final int REGISTRY_PORT = 22222;

    public static void main(String[] args)
    {

	try
	{
	    logger = Logger.getLogger("de.root1.simon.InvokeLogger");
	    Handler h;
	    h = new FileHandler("mylog.xml");
	    logger.addHandler(h);
	    logger.setLevel(Level.ALL);
	}
	catch (Exception e1)
	{
	    e1.printStackTrace();
	}

	try
	{
	    logger.info("Starting Server ...");
	    startServer();
	    logger.info("Server up and running!");
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private static void startServer() throws UnknownHostException, IOException,
	    NameBindingException
    {
	IOUtil.executeIOTask(IOController.IO_EXECUTOR, new MkDirTask(IOController.USERS_ROOT_DIR));
	// create the serverobject

	LoginService loginService = new LoginServiceImpl();
	// create the server's registry ...
	Registry registry = Simon.createRegistry(REGISTRY_PORT);

	// ... where we can bind the serverobject to

	registry.bind(LoginService.BIND_NAME, loginService);

	// some mechanism to shutdown the server should be placed here
	// this should include the following command:
	// registry.unbind("server");
	// registry.stop();
    }
}
