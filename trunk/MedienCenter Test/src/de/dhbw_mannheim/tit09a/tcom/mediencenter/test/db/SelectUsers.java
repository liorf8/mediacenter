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
		ResultSet rs = dbMan.executeStatement("SELECT * FROM Users", ResultSet.class);
		System.out.println("SELECT in " + (System.currentTimeMillis() - start));
		start = System.currentTimeMillis();

		ResultSetMetaData rsMd = rs.getMetaData();
		for(int i=1; i<=rsMd.getColumnCount(); i++)
		{
			System.out.print(rsMd.getColumnName(i)+ " ");
		}
		System.out.println();
		while(rs.next())
		{
			for(int i=1; i<=rsMd.getColumnCount(); i++)
			{
				System.out.print(rs.getString(i) +" ");
			}
			System.out.println();
		}

		rs.close();

	}

}
