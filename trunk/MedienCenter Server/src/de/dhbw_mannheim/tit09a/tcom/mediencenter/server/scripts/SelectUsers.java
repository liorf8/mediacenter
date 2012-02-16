package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.scripts;

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
		try
		{
			long start;

			start = System.currentTimeMillis();
			DatabaseManager dbMan = DatabaseManager.getInstance();
			System.out.println("Gotten instance in " + (System.currentTimeMillis() - start) + "ms");

			start = System.currentTimeMillis();
			ResultSet rs = dbMan.executeStatement("SELECT * FROM Users", ResultSet.class);
			ResultSetMetaData rsMd = rs.getMetaData();
			for (int i = 1; i <= rsMd.getColumnCount(); i++)
			{
				System.out.print(rsMd.getColumnName(i) + " ");
			}
			System.out.println();
			while (rs.next())
			{
				for (int i = 1; i <= rsMd.getColumnCount(); i++)
				{
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
			}

			rs.close();
			System.out.println("SELECT in " + (System.currentTimeMillis() - start) + "ms");
		}
		finally
		{
			DatabaseManager.getInstance().disConnect();
		}

	}

}
