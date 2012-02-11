package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.ServerUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.interfaces.Server;
import de.root1.simon.Registry;
import de.root1.simon.Simon;
import de.root1.simon.SimonRegistryStatistics;

public class ServerMain
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	public final static Logger		logger			= Logger.getLogger(ServerMain.class.getName());
	private static final Logger		invokeLogger	= Logger.getLogger("de.root1.simon.InvokeLogger");
	public static Registry			registry;
	public static boolean			isRunning;
	public static DatabaseManager	dbMan;

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
	// -- Main Method -----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void main(String[] args)
	{
		try
		{
			logger.setLevel(Level.ALL);
			logger.addHandler(new FileHandler("ServerMain.log", false));
			logger.info("ServerMain Logger started!");

			invokeLogger.setLevel(Level.ALL);
			invokeLogger.addHandler(new FileHandler("Invoke.log", false));

			// for testing purposes quickstart
			startServer();

			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (true)
			{
				System.out.println("Type in a command " + Arrays.toString(Command.values()) + ":");
				String input = br.readLine();

				if (input.equals(Command.exit.toString()))
					exit();
				else if (input.equals(Command.restart.toString()))
				{
					shutdownServer();
					startServer();
				}
				else if (input.equals(Command.shutdown.toString()))
					shutdownServer();
				else if (input.equals(Command.start.toString()))
					startServer();
				else
					printHelp();
			}

		}
		catch (Exception e)
		{
			logger.severe(e.toString());
			e.printStackTrace();
			exit();
		}
	}

	// --------------------------------------------------------------------------------
	// -- Static Method(s) ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static void startServer() throws Exception
	{
		if (isRunning == false)
		{
			logger.info("Starting Server ...");

			logger.finer("Creating directories ...");
			IOUtil.executeMkDir(UserFiles.USER_FILES_DIR);
			IOUtil.executeMkDir(DatabaseManager.DATABASE_DIR);

			logger.finer("Initialize the DatabaseManager ...");
			dbMan = DatabaseManager.getInstance();

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
			logger.info("Server shut down!");
		}
		else
		{
			System.out.println("Server already shutdown!");
		}
	}

	// --------------------------------------------------------------------------------
	private static void exit()
	{
		if (isRunning)
			shutdownServer();

		logger.info("Exiting programm. Bye!");
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
