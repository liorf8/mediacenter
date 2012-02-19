package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NamedThreadPoolFactory;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.SimonRegistryStatistics;

public class ServerMain
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	// os: C:\\Users\\mhertram\\MedienCenter
	// max: C:\\Users\\Max\\MedienCenter
	private static final String						CLASS_NAME		= ServerMain.class.getName();

	static final File								SERVER_DIR		= new File("C:\\Users\\Max\\MedienCenter\\");

	public static final Logger						MAIN_LOGGER		= LoggerFactory.getLogger(CLASS_NAME);
	private static final java.util.logging.Logger	invokeLogger	= java.util.logging.Logger.getLogger("de.root1.simon.InvokeLogger");
	private static Registry							registry;
	private static boolean							isRunning;
	private static boolean							isInitialized;
	private static ExecutorService					executor;

	public static enum Command
	{
		exit("shuts the server down, if necessary, and exits this programm"),
		restart("restarts the server (shutdown + start)"),
		shutdown("shuts the server down"),
		start("starts the server");

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
	public static class UserInputListenerTask implements Runnable
	{
		@Override
		public void run()
		{
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
			System.out.println(this.getClass().getSimpleName() +" interrupted!");
		}
	}

	// --------------------------------------------------------------------------------
	// -- Main Method -----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void main(String[] args)
	{
		try
		{
			start();
		}
		catch (Exception e1)
		{
			try
			{
				System.err.println("Caught exception when starting. Exiting...");
				exit();
			}
			catch (Exception exitExc)
			{
				System.err.println("Caught exception when exiting. System.exit(1)");
				exitExc.printStackTrace();
				System.exit(1);
			}
		}
	}

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static synchronized void init() throws Exception
	{
		if (isInitialized == false)
		{
			MAIN_LOGGER.info("Initializing Server ...");
			long start = System.currentTimeMillis();

			// Initialize Logging
			java.util.logging.Logger julogger = java.util.logging.Logger.getLogger(CLASS_NAME);
			julogger.setLevel(Level.ALL);
			julogger.addHandler(new FileHandler(CLASS_NAME + ".log", false));
			MAIN_LOGGER.info(CLASS_NAME + " Logger started!");
			invokeLogger.setLevel(Level.ALL);
			invokeLogger.addHandler(new FileHandler("de.root1.simon.InvokeLogger.log", false));

			// Make Server dir
			IOUtil.executeMkFullDirPath(SERVER_DIR);

			// Init the Managers
			MAIN_LOGGER.debug("Initialize the FileManager ...");
			Manager.getManager(FileManager_deprecated.class);
			MAIN_LOGGER.debug("Initialize the DatabaseManager ...");
			Manager.getManager(DatabaseManager.class);
			MAIN_LOGGER.debug("Initialize the Authenticator ...");
			Manager.getManager(UserManager.class);

			// Start the UserInputListener
			executor = Executors.newSingleThreadExecutor(new NamedThreadPoolFactory("ServerMain"));
			executor.execute(new UserInputListenerTask());

			MAIN_LOGGER.info("Server initialized (" + (System.currentTimeMillis() - start) + "ms)!");
			isInitialized = true;
		}
		else
		{
			System.out.println("Server already initialized!");
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public synchronized static void start() throws Exception
	{
		if (!isInitialized)
			init();

		if (!isRunning)
		{
			MAIN_LOGGER.info("Starting Server ...");
			long start = System.currentTimeMillis();

			// DatabaseManager
			DatabaseManager dbMan = Manager.getManager(DatabaseManager.class);
			dbMan.connect();

			// FileManager
			// Nothing to do yet

			MAIN_LOGGER.debug("Create the server object ...");
			Server server = new ServerImpl();

			MAIN_LOGGER.debug("Create the Simon registry @ Port " + Server.REGISTRY_PORT + " ...");
			registry = Simon.createRegistry(Server.REGISTRY_PORT);

			MAIN_LOGGER.debug("Bind the server object (" + server + ") as '" + Server.BIND_NAME + "' to the registry (" + registry + ")...");
			registry.bind(Server.BIND_NAME, server);

			MAIN_LOGGER.info("Server up and running (" + (System.currentTimeMillis() - start) + "ms)!");
			isRunning = true;
		}
		else
		{
			System.out.println("Server already running!");
		}
	}

	// --------------------------------------------------------------------------------
	public synchronized static void shutdown() throws Exception
	{
		if (isRunning)
		{
			MAIN_LOGGER.info("Shutting down Server ...");
			long start = System.currentTimeMillis();

			SimonRegistryStatistics stats = registry.getStatistics();
			MAIN_LOGGER.info(ServerUtil.remoteStatsToString(stats));
			MAIN_LOGGER.info(ServerUtil.registryStatsToString(stats));

			// some mechanism to shutdown the server should be placed here
			// this should include the following command:
			registry.unbind(ServerImpl.BIND_NAME);
			registry.stop();

			// Database Manager
			DatabaseManager dbMan = Manager.getManager(DatabaseManager.class);
			dbMan.disConnect();

			// FileManager
			// Nothing to do yet

			MAIN_LOGGER.info("Server shut down (" + (System.currentTimeMillis() - start) + "ms)!");
			isRunning = false;
		}
		else
		{
			System.out.println("Server already shutdown!");
		}
	}

	// --------------------------------------------------------------------------------
	public static synchronized void restart() throws Exception
	{
		MAIN_LOGGER.info("Restarting Server ...");
		long start = System.currentTimeMillis();
		shutdown();
		start();
		MAIN_LOGGER.info("Server restarted (" + (System.currentTimeMillis() - start) + "ms)!");
	}

	// --------------------------------------------------------------------------------
	public static synchronized void exit() throws Exception
	{
		MAIN_LOGGER.info("Exiting programm ...");
		long start = System.currentTimeMillis();

		if (isRunning)
			shutdown();

		MAIN_LOGGER.info("Shutting down " + UserInputListenerTask.class.getSimpleName() + " ...");
		if (executor != null)
			executor.shutdownNow(); // interrupts the task
		// this was the last thread alive, so the ServerMain will exit

		MAIN_LOGGER.info("Auf Wiedersehen (" + (System.currentTimeMillis() - start) + "ms)!");
	}

	// --------------------------------------------------------------------------------
	private static synchronized void printHelp()
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
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------

}
