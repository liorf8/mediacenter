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
		if (man != null)
			man.shutdown();
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected Logger	logger;
	protected boolean	isRunning;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected Manager(Level logLvl) throws Exception
	{
		onBeforeSuperInit();

		// Initialize Logger
		logger = LoggerFactory.getLogger(getClass());
		java.util.logging.Logger juLogger = java.util.logging.Logger.getLogger(getClass().getName());
		
		if (MANAGERS_LOG_LVL == null)
			juLogger.setLevel(logLvl);
		else
			juLogger.setLevel(MANAGERS_LOG_LVL);
		
		juLogger.addHandler(new FileHandler(getClass().getName() + ".log", false));
		logger.debug("{} Logger started", this);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public synchronized final boolean isRunning()
	{
		return isRunning;
	}

	// --------------------------------------------------------------------------------
	public synchronized final void start() throws Exception
	{
		if (!isRunning)
		{
			isRunning = true;
			long start = System.currentTimeMillis();
			logger.debug("Starting {} ...", this.getClass().getSimpleName());
			onStart();
			logger.info("Started {} in {}ms", this.getClass().getSimpleName(), (System.currentTimeMillis() - start));
		}
	}

	// --------------------------------------------------------------------------------
	public synchronized final void shutdown() throws Exception
	{
		if (isRunning)
		{
			isRunning = false;
			long start = System.currentTimeMillis();
			logger.debug("Shutting down {} ...", this.getClass().getSimpleName());
			onShutdown();
			logger.info("Shut down {} in {}ms", this.getClass().getSimpleName(), (System.currentTimeMillis() - start));
		}
	}

	// --------------------------------------------------------------------------------
	public synchronized final void restart() throws Exception
	{
		long start = System.currentTimeMillis();
		logger.debug("Restarting {} ...", this.getClass().getSimpleName());
		shutdown();
		start();
		logger.info("Restarted {} in {}ms", this.getClass().getSimpleName(), (System.currentTimeMillis() - start));
	}

	// --------------------------------------------------------------------------------
	public final Logger getLogger()
	{
		return logger;
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	protected abstract void onStart() throws Exception;

	// --------------------------------------------------------------------------------
	protected abstract void onShutdown() throws Exception;

	// --------------------------------------------------------------------------------
	/**
	 * May be overwritten by extending classes to execute code before the initialization of this class.
	 * 
	 * @throws Exception
	 */
	protected void onBeforeSuperInit() throws Exception
	{
		// overwrite if you need to
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
