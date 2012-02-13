package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;


public class DatabaseManager
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	// Database config
	static final File				DATABASE_DIR			= new File("C:\\Users\\Max\\MedienCenter\\DATABASE");
	private static final String		DRIVER_CLASS			= "org.hsqldb.jdbcDriver";
	private static final String		SUB_PROTOCOL			= "hsqldb";
	private static final String		SUB_NAME				= "file:" + DATABASE_DIR + ";shutdown=true";
	private static final String		URL						= String.format("jdbc:%s:%s", SUB_PROTOCOL, SUB_NAME);
	private static final String		USER					= "SA";
	private static final String		PW						= "max";

	// Connection pool config
	// after 30s throw NoSuchElementException
	private static final int		MAX_ACTIVE				= 5;
	private static final byte		WHEN_EXAUSTED_ACTION	= GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
	private static final long		MAX_WAIT				= 30 * 1000;
	private static DatabaseManager	instance;
	private final Logger			logger					= Logger.getLogger(getClass().getName());

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static synchronized DatabaseManager getInstance() throws Exception
	{
		if (instance == null)
			instance = new DatabaseManager();
		return instance;
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variable(s) --------------------------------------------------------
	// --------------------------------------------------------------------------------
	private ObjectPool<Connection>	conPool;
	private Map<String, String[]>	tables;

	// --------------------------------------------------------------------------------
	// -- Constructor(s) --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private DatabaseManager() throws Exception
	{
		try
		{
			// FIXME: Sadly the logger of HSQL sets global Formatter.
			// So only warning and severe messages are displayed in a shortened format.
			// And all loggers initialized before this command never log anything more.
			
			DriverManager.getConnection(URL, USER, PW);
			logger.setLevel(Level.ALL);
			logger.addHandler(new FileHandler(getClass().getName() + ".log", false));
			logger.info("Logger started.");
			logger.severe("Logger severely started!");

			// Check if driver class exists
			logger.finer("Loading driver class " + DRIVER_CLASS);
			Class.forName(DRIVER_CLASS);
			logger.fine("Loading driver class " + DRIVER_CLASS + " successfull");

			// DriverManager.setLogWriter(new PrintWriter(System.out));

			// Build the tables Map
			tables = new HashMap<String, String[]>();
			tables.put("user", new String[] { "user", "pw_hash", "role", "reg_date" });
			logger.fine("Tables: " + tables);

			// Build the ConnectionPool
			PoolableObjectFactory<Connection> factory = this.new ConnectionPoolFactory(URL, USER, PW);
			conPool = new GenericObjectPool<Connection>(factory, MAX_ACTIVE, WHEN_EXAUSTED_ACTION, MAX_WAIT);
		}
		catch (ClassNotFoundException e)
		{
			ServerMain.logger.severe("ERROR: failed to load HSQLDB JDBC driver: " + DRIVER_CLASS);
			throw e;
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public void shutdown()
	{
		try
		{
			conPool.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private/Package Methods -----------------------------------------------------
	// --------------------------------------------------------------------------------
	// TODO: reduce visibilities to package
	public Connection borrowConnection() throws NoSuchElementException, IllegalStateException, Exception
	{
		logger.entering("DatabaseManager", "borrowConnection");
		logger.finest("Before borrowing: active/idling Connections:" + conPool.getNumActive() + "/" + conPool.getNumIdle());
		Connection con = conPool.borrowObject();
		logger.finest("After borrowing: active/idling Connections:" + conPool.getNumActive() + "/" + conPool.getNumIdle());
		logger.exiting("DatabaseManager", "borrowConnection", con);
		logger.severe("Test logger!");
		return con;
	}

	public Connection newConnection() throws SQLException
	{
		logger.entering("DatabaseManager", "newConnection");
		Connection con = DriverManager.getConnection(URL, USER, PW);
		logger.fine("bla");
		return con;
	}

	// --------------------------------------------------------------------------------
	public void returnConnection(Connection con) throws Exception
	{
		logger.entering("DatabaseManager", "returnConnection", con);
		logger.finest("Before returning: active/idling Connections:" + conPool.getNumActive() + "/" + conPool.getNumIdle());
		conPool.returnObject(con);
		logger.finest("After returning: active/idling Connections:" + conPool.getNumActive() + "/" + conPool.getNumIdle());
		logger.exiting("DatabaseManager", "returnConnection");
	}

	// --------------------------------------------------------------------------------
	public static void createTables()
	{

	}

	// --------------------------------------------------------------------------------
	// -- Private Classes -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public class ConnectionPoolFactory implements PoolableObjectFactory<Connection>
	{
		private final String	url;
		private final String	user;
		private final String	pw;

		public ConnectionPoolFactory(String url, String user, String pw)
		{
			this.url = url;
			this.user = user;
			this.pw = pw;
		}

		@Override
		public Connection makeObject() throws Exception
		{
			logger.entering("DatabaseManager", "makeObject");
			Connection con = DriverManager.getConnection(url, user, pw);
			logger.exiting("DatabaseManager", "makeObject", con);
			return con;
		}

		@Override
		public void destroyObject(Connection con) throws Exception
		{
			logger.fine("Destroying Connection: " + con);
			con.close();
			con = null;
		}

		@Override
		public boolean validateObject(Connection con)
		{
			logger.entering("DatabaseManager", "validateObject", con);
			boolean valid = false;
			try
			{
				valid = con.isValid(10);
			}
			catch (SQLException e)
			{
				// obviously not valid
				e.printStackTrace();
			}
			logger.exiting("DatabaseManager", "validateObject", valid);
			return valid;
		}

		@Override
		public void activateObject(Connection con) throws Exception
		{
			logger.entering("DatabaseManager", "activateObject", con);
			// TODO Auto-generated method stub
		}

		@Override
		public void passivateObject(Connection con) throws Exception
		{
			logger.entering("DatabaseManager", "passivateObject", con);
			// TODO Auto-generated method stub

		}
	}
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
