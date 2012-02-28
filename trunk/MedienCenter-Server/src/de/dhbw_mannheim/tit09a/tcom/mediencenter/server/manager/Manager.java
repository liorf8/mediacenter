package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Manager
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	/**
	 * Global log level for all managers. If it is set (not null), the log levels for each manager are ignored.
	 */
	public static final Level	MANAGERS_LOG_LVL	= Level.ALL;

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static void shutdownManager(Manager man) throws Exception
	{
		if(man != null)
			man.shutdown();
	}
	
	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected final Logger		logger				= LoggerFactory.getLogger(getClass().getName());
	protected boolean			isRunning;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected Manager(Level logLvl) throws Exception
	{
		onBeforeSuperInit();

		// Initialize Logger
		java.util.logging.Logger juLogger = java.util.logging.Logger.getLogger(getClass().getName());
		if (MANAGERS_LOG_LVL == null)
			juLogger.setLevel(logLvl);
		else
			juLogger.setLevel(MANAGERS_LOG_LVL);
		juLogger.addHandler(new FileHandler(getClass().getName() + ".log", false));
		logger.info("{} Logger started", this);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public final boolean isRunning()
	{
		return isRunning;
	}

	// --------------------------------------------------------------------------------
	public final void start() throws Exception
	{
		synchronized (this)
		{
			if (!isRunning)
			{
				isRunning = true;
				long start = System.currentTimeMillis();
				logger.debug("Starting {}", this);
				onStart();
				logger.info("Started {} in {}ms", this, (System.currentTimeMillis() - start));
			}
		}
	}

	// --------------------------------------------------------------------------------
	public final void shutdown() throws Exception
	{
		synchronized (this)
		{
			if (isRunning)
			{
				isRunning = false;
				long start = System.currentTimeMillis();
				logger.debug("Shutting down {}", this);
				onShutdown();
				logger.info("Shut down {} in {}ms", this, (System.currentTimeMillis() - start));
			}
		}
	}

	// --------------------------------------------------------------------------------
	public final void restart() throws Exception
	{
		synchronized (this)
		{
			long start = System.currentTimeMillis();
			logger.debug("Restarting {}", this);
			shutdown();
			start();
			logger.info("Restarted {} in {}ms", this, (System.currentTimeMillis() - start));
		}
	}

	// --------------------------------------------------------------------------------
	public final Logger getLogger()
	{
		return logger;
	}

	// --------------------------------------------------------------------------------
	public String toString()
	{
		return getClass().getSimpleName();
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected abstract void onStart() throws Exception;

	// --------------------------------------------------------------------------------
	protected abstract void onShutdown() throws Exception;

	// --------------------------------------------------------------------------------
	/**
	 * May be overwritten by extending classes.
	 * 
	 * @throws Exception
	 */
	protected void onBeforeSuperInit() throws Exception
	{

	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
