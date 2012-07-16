package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.IOUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.NIOUtil;

public class DatabaseManager extends Manager
{
	// --------------------------------------------------------------------------------
	// -- Static Variables ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static URI	SQL_PACKAGE;
	static
	{
		try
		{
			// TODO relative links do not work in jar files
			// Does not work in jar: SQL_PACKAGE = new URI("../sql/");
			// -> moved to resources -> check if it does work now
			SQL_PACKAGE = new URI("/sql/");

		}
		catch (URISyntaxException e)
		{
			ServerMain.SERVER_LOGGER.error("Could not build URI", e);
		}
	}

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

	// Get database path and URL
	private static final Path				DB_PATH			= ServerMain.SERVER_PATH.resolve(Paths.get("DATABASE", "db"));
	private static final String				SUBNAME			= String.format("file:%s;%s", DB_PATH, OPTIONS);
	private static final String				URL				= String.format("jdbc:%s:%s", SUB_PROTOCOL, SUBNAME);

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
				{
					instance = new DatabaseManager();
				}
			}
		}
		return instance;
	}

	// --------------------------------------------------------------------------------
	public static void close(Statement st) throws SQLException
	{
		if (st != null)
			st.close();
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
	public static String getSQL(String sqlFileName) throws IOException
	{
		String sql = IOUtil.resourceToString(DatabaseManager.class, DatabaseManager.SQL_PACKAGE.resolve(sqlFileName));
		if (sql == null || sql.isEmpty())
			ServerMain.SERVER_LOGGER.error("Reading sql String of file '{}' returned null or empty: '{}'",
					DatabaseManager.SQL_PACKAGE.resolve(sqlFileName),
					sql);
		return sql;
	}

	// --------------------------------------------------------------------------------
	// -- Instance Variables ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	private Connection	clientConnection;

	// --------------------------------------------------------------------------------
	// -- Constructors ----------------------------------------------------------------
	// --------------------------------------------------------------------------------
	DatabaseManager() throws Exception
	{
		super(Level.ALL);

		logger.info("Database: {}", URL);
		logger.info("Database user: {}, password: {}", CLIENT_USER, CLIENT_PW);

		executeStatementAsDBA(getSQL("CreateTableUsers.sql"), Void.class);
		executeStatementAsDBA(getSQL("CreateTableMedia.sql"), Void.class);
		SQLCreateClientUser.main(null);

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
		NIOUtil.createDirs(DB_PATH);
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public Connection getClientConnection() throws SQLException
	{
		if (clientConnection == null)
		{
			clientConnection = getConnection(URL, CLIENT_USER, CLIENT_PW);
			logger.info("Opened new client connection: {}", clientConnection);
		}
		logger.trace("returning {}", clientConnection);
		return clientConnection;
	}

	// --------------------------------------------------------------------------------
	// -- Protected Methods -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	@Override
	protected void onBeforeSuperInit() throws Exception
	{
		// Fix for: Sadly the logger of HSQL sets global Formatter.
		// So only warning and severe messages are displayed in a shortened format.
		// And all loggers initialized before this command never log anything anymore.
		System.setProperty("hsqldb.reconfig_logging", "false");
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void onStart() throws Exception
	{
		getClientConnection();
	}

	// --------------------------------------------------------------------------------
	@Override
	protected void onShutdown() throws Exception
	{
		closeClientConnection();
	}

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
	static <T> T executeStatementAsDBA(String sql, Class<T> returnType) throws SQLException
	{
		Connection con = null;
		Statement stmt = null;
		try
		{
			T returnObj = null;
			con = getConnection(URL, DBA_USER, DBA_PW);
			con.setAutoCommit(false);
			stmt = con.createStatement();
			if (returnType.equals(Void.class) || returnType.equals(void.class))
			{
				stmt.execute(sql);
				returnObj = null;
			}
			else if (returnType.equals(Integer.class) || returnType.equals(int.class))
			{
				Integer affectedRows = stmt.executeUpdate(sql);
				returnObj = (T) affectedRows;
			}
			else if (returnType.equals(ResultSet.class))
			{
				ResultSet set = stmt.executeQuery(sql);
				returnObj = (T) set;
			}
			else
			{
				throw new UnsupportedOperationException("Operation for " + returnType + " not supported.");
			}
			con.commit();
			return returnObj;
		}
		catch (SQLException e)
		{
			System.err.println("Transaction is being rolled back.");
			rollback(con);
			throw e;
		}
		finally
		{
			close(stmt);
			if (con != null)
			{
				con.close();
			}
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private Methods -------------------------------------------------------------
	// --------------------------------------------------------------------------------
	private void closeClientConnection() throws SQLException
	{
		logger.info("Closing client connection: {}", clientConnection);
		if (clientConnection != null)
		{
			clientConnection.close();
			clientConnection = null;
		}
	}

	// --------------------------------------------------------------------------------
	private static Connection getConnection(String url, String user, String pw) throws SQLException
	{
		Connection con = DriverManager.getConnection(url, user, pw);
		con.setAutoCommit(false);
		return con;
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
