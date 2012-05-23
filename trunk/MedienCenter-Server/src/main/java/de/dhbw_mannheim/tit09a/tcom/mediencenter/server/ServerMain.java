package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.remote.SessionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.NamedThreadPoolFactory;
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

	private static enum Command
	{
		start("starts the server"),
		shutdown("shuts the server down"),
		restart("restarts the server (shutdown and start)"),
		cancel("cancels the current Task (start or shutdown). Do only cancel while counting down."),
		exit("Cancels the current Task, shuts the server down and exits this programm");

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
		new ServerMain().start();

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
	private Thread				userInputListenerThread;
	private ExecutorService		commandExecutor;
	private Future<?>			commandFuture;
	private volatile boolean	isRunning;
	private volatile boolean	shutdownAnyway;

	private RpcManager			rpcMan;
	private DatabaseManager		dbMan;
	private NFileManager		fileMan;
	private UserManager			userMan;
	private VlcManager			vlcMan;

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

			// Same for the startShutdownExecutor
			commandExecutor = Executors.newSingleThreadExecutor(new NamedThreadPoolFactory("CommandSingleThreadExecutor"));

			// Initialize the UserInputListener
			userInputListenerThread = new Thread(new UserInputListenerTask(), UserInputListenerTask.class.getSimpleName());
			userInputListenerThread.start();

			SERVER_LOGGER.info("{} initialized in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
		}
		catch (Throwable t)
		{
			shutdownAnyway = true;
			System.err.println("Caught Throwable while initializing. Exiting. Throwable:");
			t.printStackTrace();
			exit(0);
		}
	}

	private class UserInputListenerTask implements Runnable
	{
		@Override
		public void run()
		{
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while (!Thread.interrupted())
			{
				try
				{
					System.out.println("\nType in a command " + Arrays.toString(Command.values()) + ":");
					String inputLine = br.readLine();
					if (Thread.interrupted())
						break;
					String[] input = inputLine.split("\\s");
					String command = input[0];
					int delayInSeconds = 0;
					if (input.length > 1)
					{
						try
						{
							String arg1 = input[1];
							if (!arg1.isEmpty())
								delayInSeconds = Integer.valueOf(arg1);
						}
						catch (NumberFormatException e)
						{
							System.out.println(e + ". Delay in seconds expected.");
						}
					}

					if (command.equals(Command.start.toString()))
						start();
					else if (command.equals(Command.shutdown.toString()))
						shutdown(delayInSeconds);
					else if (command.equals(Command.restart.toString()))
						restart(delayInSeconds);
					else if (command.equals(Command.cancel.toString()))
						cancel();
					else if (command.equals(Command.exit.toString()))
						exit(delayInSeconds);
					else
						printHelp();

				}
				catch (IOException e)
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
			finally
			{
				System.out.println(Thread.currentThread() + " interrupted!");
			}
		}
	}

	// --------------------------------------------------------------------------------
	private void start()
	{
		if (isNoTaskRunning())
		{
			if (!isRunning)
			{
				commandFuture = commandExecutor.submit(new StartTask());
			}
			else
			{
				System.out.println(CLASS_NAME + " already running!");
			}
		}
		else
			System.out.println(CLASS_NAME + " is currently running a Task. Enter 'cancel' to abort operation.");
	}

	// --------------------------------------------------------------------------------
	private class StartTask implements Runnable
	{
		@Override
		public void run()
		{
			try
			{
				SERVER_LOGGER.info("Starting {} ...", CLASS_NAME);
				long start = System.currentTimeMillis();

				// Managers
				SERVER_LOGGER.info("Starting Managers ...");
				// for first start

				dbMan = DatabaseManager.getInstance();
				fileMan = NFileManager.getInstance();
				userMan = UserManager.getInstance();
				vlcMan = VlcManager.getInstance();
				rpcMan = RpcManager.getInstance();

				// for restart
				// order is important, because fileMan and userMan need dbMan
				dbMan.start();
				fileMan.start();
				userMan.start();
				vlcMan.start();
				rpcMan.start();

				isRunning = true;
				SERVER_LOGGER.info("Started {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
			}
			catch (InterruptedException ignore)
			{

			}
			catch (Throwable t)
			{
				shutdownAnyway = true;
				System.err.println("Caught Throwable while starting. Exiting. Throwable:");
				t.printStackTrace();
				exit(0);
			}
		}
	}

	// --------------------------------------------------------------------------------
	private void shutdown(int delayInSeconds)
	{
		if (isRunning || shutdownAnyway)
		{
			if (isNoTaskRunning())
				commandFuture = commandExecutor.submit(new ShutdownTask(delayInSeconds));
			else
				System.out.println(CLASS_NAME + " is currently running a Task. Enter 'cancel' to abort operation.");
		}
		else
		{
			System.out.println(CLASS_NAME + " already shut down!");
		}
	}

	// --------------------------------------------------------------------------------
	private class ShutdownTask implements Runnable
	{
		private int	delayInSeconds;

		public ShutdownTask(int delayInSeconds)
		{
			this.delayInSeconds = delayInSeconds;
		}

		@Override
		public void run()
		{
			try
			{
				SERVER_LOGGER.info("Shutting down {} in {} seconds...", CLASS_NAME, delayInSeconds);
				if (delayInSeconds > 0 && rpcMan != null)
				{
					// notify users
					Iterator<SessionImpl> iter = rpcMan.getServer().getSessions();
					while (iter.hasNext())
					{
						iter.next().getClientCallback().notifyShutdown(delayInSeconds);
					}
					// wait for delayInSeconds
					for (; delayInSeconds > 0; delayInSeconds--)
					{
						System.out.println(delayInSeconds);
						Thread.sleep(1000);
					}
				}

				// really shutting down if not cancelled until now
				long start = System.currentTimeMillis();

				// Shut down the managers (do not get instance because if an error occurred there it will occur again on forced shutdown)
				// And shut them down in the reversed order (dbMan at last, rpcMan first)
				SERVER_LOGGER.info("Shutting down Managers ...");
				Manager.shutdownManager(rpcMan);
				Manager.shutdownManager(vlcMan);
				Manager.shutdownManager(userMan);
				Manager.shutdownManager(fileMan);
				Manager.shutdownManager(dbMan);

				isRunning = false;
				SERVER_LOGGER.info("Shut down {} in {}ms", CLASS_NAME, (System.currentTimeMillis() - start));
			}
			catch (InterruptedException ignore)
			{

			}
			catch (Throwable t)
			{
				System.err.println("Caught Throwable on exiting. Will do System.exit(1). Throwable:");
				t.printStackTrace();
				System.exit(1);
			}
		}
	}

	// --------------------------------------------------------------------------------
	private void cancel()
	{
		SERVER_LOGGER.info("Cancelling running Task...");
		if (commandFuture != null && !commandFuture.isDone())
		{
			commandFuture.cancel(true);
			System.out.println("CommandTask cancelled!");
		}
		else
		{
			System.out.println("Cancellation of CommandTask not necessary because it is done already!");
		}
	}

	// --------------------------------------------------------------------------------
	public void restart(int delayInSeconds)
	{
		if (isNoTaskRunning())
		{
			shutdown(delayInSeconds);
			start();
		}
		else
			System.out.println(CLASS_NAME + " is currently shutting down/starting. Enter 'cancel' to abort operation.");
	}

	// --------------------------------------------------------------------------------
	public synchronized void exit(int delayInSeconds)
	{
		SERVER_LOGGER.info("Exiting programm ...");

		cancel();
		shutdown(delayInSeconds);

		// new thread so that user commands are possible while waiting
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					commandFuture.get();
					if (userInputListenerThread != null)
						userInputListenerThread.interrupt();
					if (commandExecutor != null)
						commandExecutor.shutdown();
					System.out.println("Exit almost complete! Press any key to exit UserInputListenerTask.");
				}
				catch (InterruptedException | ExecutionException | CancellationException e)
				{
					System.out.println("Shutdown aborted!");
				}
				
			}
		}).start();

	}

	// --------------------------------------------------------------------------------
	// -- Private Methods--------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private boolean isNoTaskRunning()
	{
		return (commandFuture == null || commandFuture.isDone());
	}

	// --------------------------------------------------------------------------------
	private void printHelp()
	{
		StringBuilder sb = new StringBuilder(100);
		sb.append("Usage:\n");
		for (Command oneCommand : Command.values())
		{
			sb.append(String.format("%-10s", oneCommand));
			sb.append(" - ");
			sb.append(oneCommand.getDescr());
			sb.append("\n");
		}
		sb.append("\n");
		sb.append("Specify a delay in seconds after a command to delay the shutdown/restart/exit. e.g. \"shutdown 30\")");
		System.out.println(sb);
		System.out.flush();
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
