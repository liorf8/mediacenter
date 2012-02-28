package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.UserManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.VlcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote.ServerImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.SimonRegistryStatistics;

public class ServerMain
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static final String	CLASS_NAME		= ServerMain.class.getSimpleName();
	public static Logger		INVOKE_LOGGER	= LoggerFactory.getLogger("de.root1.simon.InvokeLogger");
	public static Logger		SERVER_LOGGER	= LoggerFactory.getLogger(ServerMain.class);
	public static Path			SERVER_PATH		= Paths.get(System.getProperty("user.home"), "MedienCenter");
	private static ServerMain	instance;

	private static enum Command
	{
		start("starts the server"),
		shutdown("shuts the server down"),
		restart("restarts the server (shutdown + start)"),
		exit("shuts the server down, if necessary, and exits this programm");

		private final String	descr;

		private Command(String descr)
		{
			this.descr = descr;
		}

		public String getDescr()
		{
			return descr;
		}
	};

	// --------------------------------------------------------------------------------
	// -- Main Method -----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void main(String[] args) throws Exception
	{
		ServerMain.getInstance().start();
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Thread		userInputListenerThread;
	private Registry	registry;
	private boolean		isRunning;

	// --------------------------------------------------------------------------------
	public static synchronized ServerMain getInstance() throws Exception
	{
		if (instance == null)
			instance = new ServerMain();
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public ServerMain()
	{
		try
		{
			SERVER_LOGGER.info("Initializing {} ...", CLASS_NAME);
			long start = System.currentTimeMillis();

			// Initialize Logging
			initLogging(Level.ALL, Level.ALL);

			// Make Server directory
			SERVER_LOGGER.info("Creating server directory @ {} ...", SERVER_PATH);
			NIOUtil.createAllDirs(SERVER_PATH);

			// Initialize the UserInputListener
			userInputListenerThread = new Thread(new UserInputListenerTask(), UserInputListenerTask.class.getSimpleName());

			SERVER_LOGGER.info("{} initialized in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
		}
		catch (Throwable t)
		{
			System.err.println("Caught Throwable while initializing. Exiting. Throwable:");
			t.printStackTrace();
			exit();
		}
	}

	// --------------------------------------------------------------------------------
	public void start()
	{
		try
		{
			if (!isRunning)
			{
				synchronized (this)
				{
					isRunning = true;
					SERVER_LOGGER.info("Starting {}", CLASS_NAME);
					long start = System.currentTimeMillis();

					// Simon
					Server server = new ServerImpl();
					SERVER_LOGGER.info("Creating SIMON registry @ {}:{} ...", Server.IP, Server.REGISTRY_PORT);
					registry = Simon.createRegistry(Server.REGISTRY_PORT);

					SERVER_LOGGER.info("Binding server object {} as '{}' to registry ...", server, Server.BIND_NAME);
					registry.bind(Server.BIND_NAME, server);

					// Managers
					SERVER_LOGGER.info("Starting Managers ...");
					DatabaseManager.getInstance().start();
					NFileManager.getInstance().start();
					UserManager.getInstance().start();
					VlcManager.getInstance().start();

					// Finally start the UserInputListener
					if (!userInputListenerThread.isAlive())
						userInputListenerThread.start();

					SERVER_LOGGER.info("Started {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
				}
			}
			else
			{
				System.out.println(CLASS_NAME + " already running!");
			}
		}
		catch (Throwable t)
		{
			System.err.println("Caught Throwable while starting. Exiting. Throwable:");
			t.printStackTrace();
			exit();
		}
	}

	// --------------------------------------------------------------------------------
	// TODO: If shutdown and player is initialized, null pointer exception because not directly given from remote instance.
	// FIXME..
	public void shutdown() throws Exception
	{
		if (isRunning)
		{
			synchronized (this)
			{
				isRunning = false;
				SERVER_LOGGER.info("Shutting down {}", CLASS_NAME);
				long start = System.currentTimeMillis();

				// Shut down the managers
				SERVER_LOGGER.info("Shutting down Managers");
				DatabaseManager.getInstance().shutdown();
				NFileManager.getInstance().shutdown();
				UserManager.getInstance().shutdown();
				VlcManager.getInstance().shutdown();

				// Shut down Simon Registry
				SERVER_LOGGER.info("Shutting down SIMON");
				SimonRegistryStatistics stats = registry.getStatistics();
				SERVER_LOGGER.info(ServerUtil.remoteStatsToString(stats));
				SERVER_LOGGER.info(ServerUtil.registryStatsToString(stats));
				// some mechanism to shutdown the server should be placed here
				// this should include the following command:
				registry.unbind(ServerImpl.BIND_NAME);
				registry.stop();

				SERVER_LOGGER.info("Shut down {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
			}
		}
		else
		{
			System.out.println(CLASS_NAME + " already shut down!");
		}
	}

	// --------------------------------------------------------------------------------
	public void restart() throws Exception
	{
		synchronized (this)
		{
			SERVER_LOGGER.info("Restarting {}", CLASS_NAME);
			long start = System.currentTimeMillis();

			shutdown();
			start();

			SERVER_LOGGER.info("Restarted {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
		}
	}

	// --------------------------------------------------------------------------------
	public void exit()
	{
		try
		{
			synchronized (this)
			{
				SERVER_LOGGER.info("Exiting programm ...");
				long start = System.currentTimeMillis();

				shutdown();

				// Interrupt the UserinputListenerThread
				// This was the last thread alive, so the SuperManager will exit
				SERVER_LOGGER.info("Shutting down {} ...", UserInputListenerTask.class.getSimpleName());
				if (userInputListenerThread != null)
					userInputListenerThread.interrupt();

				SERVER_LOGGER.info("Exited in {}ms. Auf Wiedersehen!", (System.currentTimeMillis() - start));
			}
		}
		catch (Throwable t)
		{
			System.err.println("Caught Throwable on exiting. Will do System.exit(1). Throwable:");
			t.printStackTrace();
			System.exit(1);
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods--------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private void printHelp()
	{
		StringBuffer sb = new StringBuffer("Usage:\n");
		for (Command oneCommand : Command.values())
		{
			sb.append(String.format("%-8s", oneCommand));
			sb.append(" - ");
			sb.append(oneCommand.getDescr());
			sb.append("\n");
		}
		System.out.println(sb);
		System.out.flush();
	}

	// --------------------------------------------------------------------------------
	private class UserInputListenerTask implements Runnable
	{
		@Override
		public void run()
		{
			System.out.println(Thread.currentThread() +" started!");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (!Thread.interrupted())
			{
				try
				{
					System.out.println("Type in a command " + Arrays.toString(Command.values()) + ":");
					String input = br.readLine();

					if (input.equals(Command.exit.toString()))
						exit();
					else if (input.equals(Command.restart.toString()))
						restart();
					else if (input.equals(Command.shutdown.toString()))
						shutdown();
					else if (input.equals(Command.start.toString()))
						start();
					else
						printHelp();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			System.out.println(Thread.currentThread() +" interrupted!");
		}
	}

	// --------------------------------------------------------------------------------
	private void initLogging(Level superLvl, Level invokeLvl) throws SecurityException, IOException
	{
		java.util.logging.Logger juServerLogger = java.util.logging.Logger.getLogger(getClass().getName());
		juServerLogger.setLevel(superLvl);
		juServerLogger.addHandler(new FileHandler(getClass().getName() + ".log", false));

		java.util.logging.Logger juInvokeLogger = java.util.logging.Logger.getLogger("de.root1.simon.InvokeLogger");
		juInvokeLogger.setLevel(invokeLvl);
		juInvokeLogger.addHandler(new FileHandler("de.root1.simon.InvokeLogger.log", false));
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
