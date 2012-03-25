package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class SQLSelectTable
{
	public static enum Table
	{
		Users, Media
	};

	public static void main(String[] args) throws Exception
	{
		selectUsers(Table.Users);
	}

	public static void selectUsers(Table table) throws Exception
	{
		ResultSet rs = null;
		try
		{
			String sql = "SELECT * FROM " + table;
			rs = DatabaseManager.executeStatementAsDBA(sql, ResultSet.class);
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
		}
	}
}
