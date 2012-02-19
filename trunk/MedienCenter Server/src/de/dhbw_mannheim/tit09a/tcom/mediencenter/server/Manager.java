package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Manager
{
	private static Map<Class<? extends Manager>, Manager>	managers	= new HashMap<Class<? extends Manager>, Manager>();

	protected final Logger							logger		= LoggerFactory.getLogger(this.getClass().getName());

	@SuppressWarnings("unchecked")
	public static synchronized <T extends Manager> T getManager(Class<T> clazz) throws Exception
	{
		if (managers.get(clazz) == null)
			managers.put(clazz, clazz.newInstance());
		return (T) managers.get(clazz);
	}

	protected Manager() throws Exception
	{
		try
		{
			init();
		}
		catch (Exception initExc)
		{
			ServerMain.MAIN_LOGGER.error("Failed to init " +this+ ". Rolling back...", initExc);
			try
			{
				rollbackInit();	
			}
			catch (Exception rollbackExc)
			{
				ServerMain.MAIN_LOGGER.error("Failed to rollback!", rollbackExc);
				throw rollbackExc;
			}
			throw initExc;
		}
	}

	protected abstract void init() throws Exception;

	protected abstract void rollbackInit() throws Exception;

	protected void initLogging(Level lvl) throws IOException
	{
		java.util.logging.Logger julogger = java.util.logging.Logger.getLogger(getClass().getName());
		julogger.setLevel(lvl);
		julogger.addHandler(new FileHandler(getClass().getName() + ".log", false));
		logger.info("{} Logger started", getClass().getName());
	}
	
	public Logger getLogger()
	{
		return logger;
	}

}
