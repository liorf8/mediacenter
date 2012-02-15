package de.dhbw_mannheim.tit09a.tcom.mediencenter.server;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class DatabaseManager
{
	// --------------------------------------------------------------------------------
	// -- Static Variable(s) ----------------------------------------------------------
	// --------------------------------------------------------------------------------
	// SQL scripts
	public static final String		SQL_STMTS_PATH	= "/de/dhbw_mannheim/tit09a/tcom/mediencenter/server/sql/";

	// Database config
	static final String				DRIVER_CLASS	= "org.hsqldb.jdbcDriver";
	static final File				DATABASE_DIR	= new File("C:\\Users\\Max\\MedienCenter\\DATABASE\\db");
	static final String				SUB_PROTOCOL	= "hsqldb";
	static final String				OPTIONS			= "shutdown=true;ifexists=false;";
	// shutdown=true -> shut the database if no Connection is still open
	// ifexists=true -> throw an Exception if the db does not exist
	private static final String		SUB_NAME		= String.format("file:%s;%s", DATABASE_DIR, OPTIONS);
	private static final String		URL				= String.format("jdbc:%s:%s", SUB_PROTOCOL, SUB_NAME);

	// Users
	private static final String		ADMIN			= "SA";
	private static final String		ADMIN_PW		= "max";
	// TODO: use the User user for user activities, not the admin!
	static final String				USER			= "user";
	static final String				USER_PW			= "max";

	private static DatabaseManager	instance;

	public static enum StatementType
	{
		UPDATE, QUERY, STATEMENT
	};

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
	private Connection				con;
	private final Logger			logger	= Logger.getLogger(getClass().getName());

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
				logger.addHandler(new FileHandler(getClass().getName() + ".log", false));
				logger.info("Logger started.");
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

			// Init the connection
			try
			{
				IOUtil.executeMkDir(DatabaseManager.DATABASE_DIR);
				getAdminConnection();
			}
			catch (SQLException e)
			{
				throw new Exception("Failed to connect to database", e);
			}
		}
		catch (Exception e)
		{
			throw new Exception("DatabaseManager.<init> failed: " + e.getMessage(), e.getCause());
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Methods --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public void closeConnection()
	{
		try
		{
			if (con != null)
			{
				con.close();
				con = null;
			}
		}
		catch (SQLException e)
		{
			logger.severe(e.toString());
			e.printStackTrace();
		}
	}

	// --------------------------------------------------------------------------------
	// -- Private/Package Methods -----------------------------------------------------
	// --------------------------------------------------------------------------------
	public Connection getAdminConnection() throws SQLException
	{
		logger.entering("DatabaseManager", "newConnection");
		if (con == null)
		{
			con = DriverManager.getConnection(URL, ADMIN, ADMIN_PW);
			con.setAutoCommit(false);
		}
		logger.exiting("DatabaseManager", "newConnection", con);
		return con;
	}

	// --------------------------------------------------------------------------------
	public void createTables(boolean drop) throws SQLException, IOException
	{
		logger.entering("DatabaseManager", "createTables");
		if (drop)
		{
			executeStatementFromResource(SQL_STMTS_PATH + "DropTableUsers.sql", void.class);
		}
		executeStatementFromResource(SQL_STMTS_PATH + "CreateTableUsers.sql", void.class);
		logger.exiting("DatabaseManager", "createTables");
	}

	// --------------------------------------------------------------------------------
	public int registerUser(String login, String pw) throws SQLException, IOException
	{
		String sql = IOUtil.resourceToString(SQL_STMTS_PATH + "TmplInsertUser.sql");
		sql = String.format(sql, login, "hashed" + pw);
		return executeStatement(sql, int.class);
	}

	// --------------------------------------------------------------------------------
	public int putAttr(long id, String key, String newValue) throws IOException, SQLException
	{
		String sql = IOUtil.resourceToString(SQL_STMTS_PATH + "TmplInsertUser.sql");
		sql = String.format(sql, key, newValue, id);
		return executeStatement(sql, int.class);
	}

	// --------------------------------------------------------------------------------
	public Map<String, String> getAllAttrs(long id) throws IOException, SQLException
	{
		String sql = IOUtil.resourceToString(SQL_STMTS_PATH + "TmplGetUserAttrs.sql");
		sql = String.format(sql, "*", id);
		ResultSet rs = executeStatement(sql, ResultSet.class);
		if (rs.next())
		{
			ResultSetMetaData rsMd = rs.getMetaData();
			int columnCount = rsMd.getColumnCount();
			if (columnCount < 1)
				return Collections.emptyMap();
			Map<String, String> attrs = new HashMap<String, String>(columnCount);
			for (int i = 1; i <= rsMd.getColumnCount(); i++)
			{
				attrs.put(rsMd.getColumnName(i), rs.getString(i));
			}
			if (rs.next())
				throw new SQLException("Only one row should be returned!");
			else
				return attrs;
		}
		return Collections.emptyMap();
	}

	// --------------------------------------------------------------------------------
	public String getAttr(long id, String key) throws IOException, SQLException
	{
		String sql = IOUtil.resourceToString(SQL_STMTS_PATH + "TmplGetUserAttrs.sql");
		sql = String.format(sql, key, id);
		ResultSet rs = executeStatement(sql, ResultSet.class);
		if (rs.next())
		{
			if (rs.getMetaData().getColumnCount() > 1)
				throw new SQLException("Only one column should be returned!");
			String value = rs.getString(1);
			if (rs.next())
				throw new SQLException("Only one row should be returned!");
			else
				return value;
		}
		return null;
	}

	// TODO: reduce visibility to private
	// --------------------------------------------------------------------------------
	public <T> T executeStatementFromResource(String path, Class<T> returnType) throws SQLException, IOException
	{
		return executeStatement(IOUtil.resourceToString(path), returnType);
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
	public <T> T executeStatement(String sql, Class<T> returnType) throws SQLException
	{
		logger.entering("DatabaseManager", "executeStatement", sql);
		Connection con = null;
		try
		{
			con = getAdminConnection();
			Statement stmt = con.createStatement();
			if (returnType.equals(Void.class) || returnType.equals(void.class))
			{
				stmt.execute(sql);
				con.commit();
				logger.exiting("DatabaseManager", "executeStatement");
				return null;
			}
			else if (returnType.equals(Integer.class) || returnType.equals(int.class))
			{
				Integer affectedRows = stmt.executeUpdate(sql);
				con.commit();
				logger.exiting("DatabaseManager", "executeStatement", affectedRows);
				return (T) affectedRows;
			}
			else if (returnType.equals(ResultSet.class))
			{
				ResultSet set = stmt.executeQuery(sql);
				con.commit();
				logger.exiting("DatabaseManager", "executeStatement", set);
				return (T) set;
			}
			else
			{
				throw new IllegalArgumentException("Operation for " + returnType + " not supported.");
			}

		}
		catch (SQLException e)
		{
			if (con != null)
			{
				System.err.print("Transaction is being " + "rolled back due to:\n");
				con.rollback();
			}
			throw e;
		}
	}

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
