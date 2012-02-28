package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;


public class SQLSelectUsers
{
	public static void main(String[] args) throws Exception
	{
		selectUsers();
	}
	
	public static void selectUsers() throws Exception
	{
		ResultSet rs = null;
		DatabaseManager dbMan = null;
		try
		{
			dbMan = DatabaseManager.getInstance();
			rs = dbMan.executeStatementAsDBA("SELECT * FROM Users", ResultSet.class);
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
		}
		finally
		{
			DatabaseManager.close(rs);
			if (dbMan != null)
				dbMan.shutdown();
		}
	}

}
