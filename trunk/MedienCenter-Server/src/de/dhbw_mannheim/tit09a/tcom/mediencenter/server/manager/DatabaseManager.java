package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.NIOUtil;

public class DatabaseManager extends Manager
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	// SQL scripts
	public static final String	SQL_STMTS_PATH	= "/de/dhbw_mannheim/tit09a/tcom/mediencenter/server/sql/";

	public static enum StatementType
	{
		UPDATE, QUERY, STATEMENT
	};

	// Database config
	private static final String				DRIVER_CLASS	= "org.hsqldb.jdbcDriver";
	private static final String				SUB_PROTOCOL	= "hsqldb";
	private static final String				OPTIONS			= "shutdown=true;ifexists=false;";
	// shutdown=true -> shut the database if no Connection is still open
	// ifexists=true -> throw an Exception if the db does not exist

	// User
	private static final String				DBA_USER		= "SA";
	private static final String				DBA_PW			= "";
	private static final String				CLIENT_USER		= "CLIENT";
	private static final String				CLIENT_PW		= "max";

	public volatile static DatabaseManager	instance;

	// --------------------------------------------------------------------------------
	// -- Static Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static DatabaseManager getInstance() throws Exception
	{
		if (instance == null)
		{
			synchronized (DatabaseManager.class)
			{
				if (instance == null)
					instance = new DatabaseManager();
			}
		}
		return instance;
	}

	// --------------------------------------------------------------------------------
	public static void close(Statement ps) throws SQLException
	{
		if (ps != null)
			ps.close();
	}

	// --------------------------------------------------------------------------------
	public static void close(ResultSet rs) throws SQLException
	{
		if (rs != null)
			rs.close();
	}

	// --------------------------------------------------------------------------------
	public static void rollback(Connection con) throws SQLException
	{
		if (con != null)
			con.rollback();
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Connection		connection;
	private final Path		databasePath;
	private final String	url;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	DatabaseManager() throws Exception
	{
		super(Level.ALL);

		databasePath = ServerMain.SERVER_PATH.resolve(Paths.get("DATABASE", "db"));
		String subName = String.format("file:%s;%s", databasePath, OPTIONS);
		url = String.format("jdbc:%s:%s", SUB_PROTOCOL, subName);
		// Check if HSQLDB driver class exists
		try
		{
			logger.trace("Loading driver class " + DRIVER_CLASS);
			Class.forName(DRIVER_CLASS);
		}
		catch (ClassNotFoundException e)
		{
			throw new Exception("Failed to load HSQLDB JDBC driver: " + DRIVER_CLASS, e);
		}

		// Create the Database directory
		NIOUtil.createAllDirs(databasePath);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public Connection getConnection() throws SQLException
	{
		return getConnection(CLIENT_USER, CLIENT_PW);
	}

	// --------------------------------------------------------------------------------
	public Connection getConnection(String user, String pw) throws SQLException
	{
		logger.debug("ENTRY");
		if (connection == null)
		{
			logger.debug("Invoking DriverManager.getConnection({},{},{})", new Object[] { url, user, pw });
			connection = DriverManager.getConnection(url, user, pw);
			connection.setAutoCommit(false);
		}
		logger.debug("EXIT {}", connection);
		return connection;
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	protected void onBeforeSuperInit() throws Exception
	{
		// Fix for: Sadly the logger of HSQL sets global Formatter.
		// So only warning and severe messages are displayed in a shortened format.
		// And all loggers initialized before this command never log anything more.
		System.setProperty("hsqldb.reconfig_logging", "false");
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void onStart() throws Exception
	{
		getConnection();
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void onShutdown() throws Exception
	{
		closeConnection();
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private void closeConnection() throws SQLException
	{
		if (connection != null)
		{
			connection.close();
			connection = null;
		}
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
	protected <T> T executeStatementAsDBA(String sql, Class<T> returnType) throws SQLException
	{
		logger.debug("ENTRY {} {}", new Object[] { sql, returnType });
		Connection con = null;
		try
		{
			T returnObj = null;
			con = getConnection(DBA_USER, DBA_PW);
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
			logger.debug("EXIT {}", returnObj);
			return returnObj;

		}
		catch (SQLException e)
		{
			System.err.println("Transaction is being rolled back.");
			rollback(con);
			throw e;
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
