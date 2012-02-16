package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.Authenticator;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

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
				System.out.println("id:" +Authenticator.getInstance().insertUser(con, login+i, pw));
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
					dbMan.disConnect();
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}

			}
		}
	}
}
