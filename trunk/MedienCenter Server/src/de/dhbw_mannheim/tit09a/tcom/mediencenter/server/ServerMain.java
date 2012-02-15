package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

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
	public static final Logger		logger			= Logger.getLogger(ServerMain.class.getName());
	private static final Logger		invokeLogger	= Logger.getLogger("de.root1.simon.InvokeLogger");
	private static Registry			registry;
	private static boolean			isRunning;
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

	public static class WaitForUserInputTask implements Runnable
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
					{
						restartServer();
					}
					else if (input.equals(Command.shutdown.toString()))
						shutdownServer();
					else if (input.equals(Command.start.toString()))
						startServer();
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
			initServer();
			startServer();
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
			exit();
		}
	}

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void initServer() throws Exception
	{
		try
		{
			logger.setLevel(Level.ALL);
			logger.addHandler(new FileHandler(ServerMain.class.getName() + "log", false));
			logger.info("ServerMain Logger started!");

			invokeLogger.setLevel(Level.ALL);
			invokeLogger.addHandler(new FileHandler("de.root1.simon.InvokeLogger.log", false));

			executor = Executors.newSingleThreadExecutor(new NamedThreadPoolFactory("ServerMain"));
			executor.execute(new WaitForUserInputTask());
		}
		catch (Exception e)
		{
			logger.severe(e.toString());
			throw e;
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void startServer() throws Exception
	{
		if (isRunning == false)
		{
			logger.info("Starting Server ...");

			logger.finer("Initialize the FileManager ...");
			FileManager.getInstance();
			
			logger.finer("Initialize the DatabaseManager ...");
			DatabaseManager.getInstance();
			
			logger.finer("Create the server object ...");
			Server server = new ServerImpl();

			logger.finer("Create the Simon registry @ Port " + Server.REGISTRY_PORT + " ...");
			registry = Simon.createRegistry(Server.REGISTRY_PORT);

			logger.finer("Bind the server object (" + server + ") as '" + Server.BIND_NAME + "' to the registry (" + registry + ")...");
			registry.bind(Server.BIND_NAME, server);
			isRunning = true;

			logger.info("Server up and running!");
		}
		else
		{
			System.out.println("Server already running!");
		}
	}

	// --------------------------------------------------------------------------------
	public static void shutdownServer()
	{
		if (isRunning)
		{
			logger.info("Shutting down Server ...");

			SimonRegistryStatistics stats = registry.getStatistics();
			logger.info(ServerUtil.remoteStatsToString(stats));
			logger.info(ServerUtil.registryStatsToString(stats));

			// some mechanism to shutdown the server should be placed here
			// this should include the following command:
			registry.unbind(ServerImpl.BIND_NAME);
			registry.stop();
			isRunning = false;
			
			try
			{
				DatabaseManager.getInstance().closeConnection();
			}
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			logger.info("Server shut down!");
		}
		else
		{
			System.out.println("Server already shutdown!");
		}
	}

	public static void restartServer() throws Exception
	{
		logger.info("Restarting Server ...");
		shutdownServer();
		startServer();
	}
	// --------------------------------------------------------------------------------
	public static void exit()
	{
		if (isRunning)
			shutdownServer();

		logger.info("Exiting programm. Bye!");
		executor.shutdownNow();
		System.exit(0);
	}

	// --------------------------------------------------------------------------------
	private static void printHelp()
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
