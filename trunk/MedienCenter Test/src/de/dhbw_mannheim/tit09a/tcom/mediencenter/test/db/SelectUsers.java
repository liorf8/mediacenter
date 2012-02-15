package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

public class SelectUsers
{

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception
	{
		long start;

		start = System.currentTimeMillis();
		DatabaseManager dbMan = DatabaseManager.getInstance();
		System.out.println("Gotten instance in " + (System.currentTimeMillis() - start));

		start = System.currentTimeMillis();
		ResultSet rs = dbMan.executeStatement("SELECT * FROM USERS", ResultSet.class);
		System.out.println("SELECT in " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		String formatString = "%-10s%-15s%-15s%-25s%n";
		ResultSetMetaData rsMd = rs.getMetaData();
		System.out.printf(formatString, rsMd.getColumnName(1), rsMd.getColumnName(2), rsMd.getColumnName(3), rsMd.getColumnName(4));
		while (rs.next())
		{
			System.out.printf(formatString, rs.getInt(1), rs.getString(2), rs.getString(3), rs.getTimestamp(4));
		}

	}

}
