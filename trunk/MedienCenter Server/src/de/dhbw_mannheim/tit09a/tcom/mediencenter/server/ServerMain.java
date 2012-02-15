package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	private static final String		CLASS_NAME		= ServerMain.class.getName();
	
	static final File				SERVER_DIR		= new File("C:\\Users\\Max\\MedienCenter\\");

	public static final Logger		logger			= Logger.getLogger(CLASS_NAME);
	private static final Logger		invokeLogger	= Logger.getLogger("de.root1.simon.InvokeLogger");
	private static Registry			registry;
	private static boolean			isRunning;
	private static boolean			isInitialized;
	private static ExecutorService	executor;


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
			e1.printStackTrace();
			try
			{
				exit();
			}
			catch (Exception e)
			{
				e.printStackTrace();
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
			logger.info("Initializing Server ...");
			long start = System.currentTimeMillis();

			// Initialize Logging
			logger.setLevel(Level.ALL);
			logger.addHandler(new FileHandler(CLASS_NAME + ".log", false));
			logger.info(CLASS_NAME + " Logger started!");
			invokeLogger.setLevel(Level.ALL);
			invokeLogger.addHandler(new FileHandler("de.root1.simon.InvokeLogger.log", false));

			// Make Server dir
			IOUtil.executeMkDirRecursively(SERVER_DIR);

			// Init the Managers
			logger.finer("Initialize the FileManager ...");
			FileManager.getInstance();
			logger.finer("Initialize the DatabaseManager ...");
			DatabaseManager.getInstance();
			logger.finer("Initialize the Authenticator ...");
			Authenticator.getInstance();

			// Start the UserInputListener
			executor = Executors.newSingleThreadExecutor(new NamedThreadPoolFactory("ServerMain"));
			executor.execute(new UserInputListenerTask());

			logger.info("Server initialized (" + (System.currentTimeMillis() - start) + "ms)!");
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
			logger.info("Starting Server ...");
			long start = System.currentTimeMillis();

			// DatabaseManager
			DatabaseManager.getInstance().connect();

			// FileManager
			// Nothing to do yet

			logger.finer("Create the server object ...");
			Server server = new ServerImpl();

			logger.finer("Create the Simon registry @ Port " + Server.REGISTRY_PORT + " ...");
			registry = Simon.createRegistry(Server.REGISTRY_PORT);

			logger.finer("Bind the server object (" + server + ") as '" + Server.BIND_NAME + "' to the registry (" + registry + ")...");
			registry.bind(Server.BIND_NAME, server);

			logger.info("Server up and running (" + (System.currentTimeMillis() - start) + "ms)!");
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
			logger.info("Shutting down Server ...");
			long start = System.currentTimeMillis();

			SimonRegistryStatistics stats = registry.getStatistics();
			logger.info(ServerUtil.remoteStatsToString(stats));
			logger.info(ServerUtil.registryStatsToString(stats));

			// some mechanism to shutdown the server should be placed here
			// this should include the following command:
			registry.unbind(ServerImpl.BIND_NAME);
			registry.stop();

			// Database Manager
			DatabaseManager.getInstance().disConnect();

			// FileManager
			// Nothing to do yet

			logger.info("Server shut down (" + (System.currentTimeMillis() - start) + "ms)!");
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
		logger.info("Restarting Server ...");
		long start = System.currentTimeMillis();
		shutdown();
		start();
		logger.info("Server restarted (" + (System.currentTimeMillis() - start) + "ms)!");
	}

	// --------------------------------------------------------------------------------
	public static synchronized void exit() throws Exception
	{
		logger.info("Exiting programm ...");
		long start = System.currentTimeMillis();

		if (isRunning)
			shutdown();

		logger.info("Shutting down " + UserInputListenerTask.class.getSimpleName() + " ...");
		if (executor != null)
			executor.shutdownNow(); // interrupts the task
		// this was the last thread alive, so the ServerMain will exit

		logger.info("Auf Wiedersehen (" + (System.currentTimeMillis() - start) + "ms)!");
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
