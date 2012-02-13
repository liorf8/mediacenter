package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.logging.Logger;

import org.hsqldb.lib.FrameworkLogger;
import org.slf4j.LoggerFactory;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

public class DriverManagerTest
{
	static final File			DATABASE_DIR	= new File("C:\\Users\\Max\\MedienCenter\\DATABASE");
	private static final String	DRIVER_CLASS	= "org.hsqldb.jdbcDriver";
	private static final String	SUB_PROTOCOL	= "hsqldb";
	private static final String	SUB_NAME		= "file:" + DATABASE_DIR + ";shutdown=true";
	private static final String	USER			= "SA";
	private static final String	PW				= "max";

	public static void main(String[] args)
	{
		try
		{
			// for (Enumeration<Driver> e = DriverManager.getDrivers(); e.hasMoreElements();)
			// System.out.println(e.nextElement().getClass().getName());

			String url = String.format("jdbc:%s:%s", SUB_PROTOCOL, SUB_NAME);
			Connection con = null;
			DatabaseManager dbMan = DatabaseManager.getInstance();
			con = dbMan.borrowConnection();
			dbMan.returnConnection(con);

			System.out.println("Gotten con:" + con);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

}
