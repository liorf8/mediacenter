package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class DatabaseManager
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private static final String	CLASS_NAME		= DatabaseManager.class.getName();
	// SQL scripts
	public static final String	SQL_STMTS_PATH	= "/de/dhbw_mannheim/tit09a/tcom/mediencenter/server/sql/";

	// Database config
	public static final String	DRIVER_CLASS	= "org.hsqldb.jdbcDriver";
	public static final File	DATABASE_DIR	= new File(ServerMain.SERVER_DIR, "DATABASE" + File.separator + "db");
	public static final String	SUB_PROTOCOL	= "hsqldb";
	public static final String	OPTIONS			= "shutdown=true;ifexists=false;";
	// shutdown=true -> shut the database if no Connection is still open
	// ifexists=true -> throw an Exception if the db does not exist
	public static final String	SUB_NAME		= String.format("file:%s;%s", DATABASE_DIR, OPTIONS);
	public static final String	URL				= String.format("jdbc:%s:%s", SUB_PROTOCOL, SUB_NAME);

	public static enum StatementType
	{
		UPDATE, QUERY, STATEMENT
	};

	// User
	// TODO: use the User user for user activities, not the DBA!
	private static final String		CLIENT_USER	= "SA";
	private static final String		CLIENT_PW	= "";

	private static DatabaseManager	instance;

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
	private Connection		connection;
	private final Logger	logger	= Logger.getLogger(CLASS_NAME);

	// --------------------------------------------------------------------------------
	// -- Constructor(s) --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private DatabaseManager() throws Exception
	{
		try
		{
			// Initialize logging
			try
			{
				System.setProperty("hsqldb.reconfig_logging", "false");
				// Fix for: Sadly the logger of HSQL sets global Formatter.
				// So only warning and severe messages are displayed in a shortened format.
				// And all loggers initialized before this command never log anything more.
				logger.setLevel(Level.ALL);
				logger.addHandler(new FileHandler(CLASS_NAME + ".log", false));
				logger.info(CLASS_NAME + " Logger started!");
			}
			catch (IOException e)
			{
				throw new Exception("Could not establish logging", e);
			}

			// Check if HSQLDB driver class exists
			try
			{
				logger.finer("Loading driver class " + DRIVER_CLASS);
				Class.forName(DRIVER_CLASS);
				logger.fine("Loading driver class " + DRIVER_CLASS + " successfull");
			}
			catch (ClassNotFoundException e)
			{
				throw new Exception("Failed to load HSQLDB JDBC driver: " + DRIVER_CLASS, e);
			}

			// Create the Database directory
			try
			{
				IOUtil.executeMkDirRecursively(DatabaseManager.DATABASE_DIR);
			}
			catch (IOException e)
			{
				throw new Exception("Failed create the directory", e);
			}

			// Initial connect
			connect();
		}
		catch (Exception e)
		{
			disConnect();
			throw new Exception(this.getClass().getSimpleName() + " <init> failed: " + e.getMessage(), e.getCause());
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public synchronized void connect() throws SQLException
	{
		if (connection == null)
		{
			try
			{
				getConnection();
			}
			catch (SQLException e)
			{
				throw new SQLException("Failed to connect to database", e);
			}
		}
	}

	// --------------------------------------------------------------------------------
	public synchronized void disConnect() throws SQLException
	{
		if (connection != null)
		{
			try
			{
				connection.close();
				connection = null;
			}
			catch (SQLException e)
			{
				throw new SQLException("Failed to disconnect from database", e);
			}
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private/Package Methods -----------------------------------------------------
	// --------------------------------------------------------------------------------
	public Connection getConnection() throws SQLException
	{
		logger.entering(CLASS_NAME, "getConnection");
		if (connection == null)
		{
			logger.finer(String.format("Invoking DriverManager.getConnection(%s,%s,%s)", URL, CLIENT_USER, CLIENT_PW));
			connection = DriverManager.getConnection(URL, CLIENT_USER, CLIENT_PW);
		}
		logger.exiting(CLASS_NAME, "getConnection", connection);
		return connection;
	}

	// --------------------------------------------------------------------------------
	public void createTables(boolean drop) throws SQLException, IOException
	{
		logger.entering(CLASS_NAME, "createTables", drop);
		if (drop)
		{
			executeStatement(IOUtil.resourceToString(SQL_STMTS_PATH + "DropTableUsers.sql"), void.class);
		}
		executeStatement(IOUtil.resourceToString(SQL_STMTS_PATH + "CreateTableUsers.sql"), void.class);
		logger.exiting(CLASS_NAME, "createTables");
	}

	// TODO: reduce visibility to private
	// --------------------------------------------------------------------------------
	/**
	 * @param sql
	 *            The SQL statement.
	 * @param returnType
	 *            Void.class or void.class for executeStatement(sql). Integer.class or int.class for executeUpdate(sql). ResultSet.class for
	 *            executeQuery(sql).
	 * @return void, affectedRows or ResultSet
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public <T> T executeStatement(String sql, Class<T> returnType) throws SQLException
	{
		logger.entering(CLASS_NAME, "executeStatement", sql);
		Connection con = null;
		try
		{
			T returnObj = null;
			con = getConnection();
			con.setAutoCommit(false);
			Statement stmt = con.createStatement();
			if (returnType.equals(Void.class) || returnType.equals(void.class))
			{
				stmt.execute(sql);
				con.commit();
				returnObj = null;
			}
			else if (returnType.equals(Integer.class) || returnType.equals(int.class))
			{
				Integer affectedRows = stmt.executeUpdate(sql);
				con.commit();
				returnObj = (T) affectedRows;
			}
			else if (returnType.equals(ResultSet.class))
			{
				ResultSet set = stmt.executeQuery(sql);
				con.commit();
				returnObj = (T) set;
			}
			else
			{
				throw new IllegalArgumentException("Operation for " + returnType + " not supported.");
			}
			logger.exiting(CLASS_NAME, "executeStatement", returnObj);
			return returnObj;

		}
		catch (SQLException e)
		{
			if (con != null)
			{
				System.err.print("Transaction is being rolled back due to:\n");
				con.rollback();
			}
			throw e;
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
