package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import java.sql.SQLException;
import java.util.NoSuchElementException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

public class SimpleQuery
{
	public static void main(String[] args) throws NoSuchElementException, IllegalStateException, Exception
	{
		DatabaseManager dbMan = null;
		try
		{
			long start = System.currentTimeMillis();
			dbMan = DatabaseManager.getInstance();
			System.out.println("Gotten instance in " + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis() ;
			System.out.println(dbMan.registerUser("max1", "pw"));
			System.out.println("Registered in " + (System.currentTimeMillis() - start));
			start = System.currentTimeMillis() ;
			System.out.println(dbMan.registerUser("karo", "<3"));
			System.out.println("Registered in " + (System.currentTimeMillis() - start));
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dbMan != null)
			{
				try
				{
					dbMan.closeConnection();
				}
				catch (Exception e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}
}
