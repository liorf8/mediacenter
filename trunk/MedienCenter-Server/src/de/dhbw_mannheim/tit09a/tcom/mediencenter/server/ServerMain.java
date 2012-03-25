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
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.Manager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.RpcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.UserManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.VlcManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;

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

		/*
		 * Ausführen per cmd:
		 * 
		 * 1. cmd als Admin starten, damit ein Logfile angelegt werden kann. Diese werden im "C:\Windows\SysWOW64"-Verzeichnis angelegt. -> TODO (vll
		 * in bat ausführen) 2. Wichtig: In einer 32bit VM ausführen, wenn VLC 32bit ist. Für 64bit genau dasselbe. 3. Etwa diesen Code abschicken:
		 * C:\Windows\system32>"C:\Program Files (x86)\Java\jre7\bin\java.exe" -jar C:\Users\Max\Desktop\MedienCenter-Server.jar
		 */
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Thread			userInputListenerThread;
	private boolean			isRunning;

	private RpcManager		rpcMan;
	private DatabaseManager	dbMan;
	private NFileManager	fileMan;
	private UserManager		userMan;
	private VlcManager		vlcMan;

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
			NIOUtil.createDirs(SERVER_PATH);
			SERVER_LOGGER.info("Server directory @ {}", SERVER_PATH);

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
	public synchronized void start()
	{
		try
		{
			if (!isRunning)
			{
				isRunning = true;
				SERVER_LOGGER.info("Starting {} ...", CLASS_NAME);
				long start = System.currentTimeMillis();

				// Managers
				SERVER_LOGGER.info("Starting Managers ...");
				fileMan = NFileManager.getInstance();
				dbMan = DatabaseManager.getInstance();
				vlcMan = VlcManager.getInstance();
				userMan = UserManager.getInstance();
				rpcMan = RpcManager.getInstance();
				
				// (for restart)
				fileMan.start();
				dbMan.start();
				vlcMan.start();
				userMan.start();
				rpcMan.start();


				// Finally start the UserInputListener
				if (!userInputListenerThread.isAlive())
					userInputListenerThread.start();

				SERVER_LOGGER.info("Started {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
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
	public synchronized void shutdown() throws Exception
	{
		if (isRunning)
		{
			isRunning = false;
			SERVER_LOGGER.info("Shutting down {} ...", CLASS_NAME);
			long start = System.currentTimeMillis();

			// Shut down the managers (do not get instance because if an error occurred there it will occurr again on forced shutdown)
			SERVER_LOGGER.info("Shutting down Managers ...");
			Manager.shutdownManager(rpcMan);
			Manager.shutdownManager(userMan);
			Manager.shutdownManager(vlcMan);
			Manager.shutdownManager(dbMan);
			Manager.shutdownManager(fileMan);

			SERVER_LOGGER.info("Shut down {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
		}
		else
		{
			System.out.println(CLASS_NAME + " already shut down!");
		}
	}

	// --------------------------------------------------------------------------------
	public synchronized void restart() throws Exception
	{
		SERVER_LOGGER.info("Restarting {} ...", CLASS_NAME);
		long start = System.currentTimeMillis();

		shutdown();
		start();

		SERVER_LOGGER.info("Restarted {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
	}

	// --------------------------------------------------------------------------------
	public synchronized void exit()
	{
		try
		{
			SERVER_LOGGER.info("Exiting programm ...");
			long start = System.currentTimeMillis();

			shutdown();

			// Interrupt the UserinputListenerThread
			// This was the last thread alive, so the SuperManager will exit
			SERVER_LOGGER.info("Shutting down {} ...", UserInputListenerTask.class.getSimpleName());
			if (userInputListenerThread != null)
				userInputListenerThread.interrupt();

			SERVER_LOGGER.info("Exited in {}ms. Auf Wiedersehen! (Programm exits as soon as all Threads are closed - be patient)",
					(System.currentTimeMillis() - start));
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
		StringBuilder sb = new StringBuilder("Usage:\n");
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
			System.out.println(Thread.currentThread() + " started!");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (!Thread.interrupted())
			{
				try
				{
					System.out.println("\nType in a command " + Arrays.toString(Command.values()) + ":");
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
			try
			{
				if (br != null)
					br.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread() + " interrupted!");
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
