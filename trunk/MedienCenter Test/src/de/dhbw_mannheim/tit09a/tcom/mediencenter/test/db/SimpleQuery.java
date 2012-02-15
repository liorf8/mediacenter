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

			long start = System.currentTimeMillis();
			dbMan = DatabaseManager.getInstance();
			System.out.println("Gotten instance in " + (System.currentTimeMillis() - start));

			Connection con = dbMan.getConnection();
			System.out.println(Authenticator.getInstance().userExists(con, login));
			boolean inserted = Authenticator.getInstance().insertUser(con, login, pw);
			System.out.println(inserted);
			
			System.out.println(Authenticator.getInstance().userExists(con, login));
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
		System.out.println(Authenticator.getInstance().userExists(dbMan.getConnection(), login));
	}
}
