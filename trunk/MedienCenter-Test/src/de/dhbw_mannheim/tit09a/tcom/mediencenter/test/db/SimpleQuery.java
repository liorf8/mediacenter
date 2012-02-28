package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.UserManager;

public class SimpleQuery
{
	public static void main(String[] args) throws NoSuchElementException, IllegalStateException, Exception
	{
		DatabaseManager dbMan = null;
		String login = "Max";
		String pw = "pw";
		
		try
		{
			long start;
			start = System.currentTimeMillis();
			dbMan = DatabaseManager.getInstance();
			System.out.println("Gotten instance in " + (System.currentTimeMillis() - start));

			Connection con = dbMan.getConnection();
			start = System.currentTimeMillis();
			for(int i=0; i<100; i++)
			{
				System.out.println("id:" +UserManager.getInstance().insertUser(con, login+i, pw));
			}
			System.out.println("New users in " + (System.currentTimeMillis() - start));
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
					dbMan.shutdown();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}
	}
}
