package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.NoSuchElementException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

public class SimpleQuery
{
	public static void main(String[] args) throws NoSuchElementException, IllegalStateException, Exception
	{
		Connection con = null;
		DatabaseManager dbMan = null;
		try
		{
			dbMan = DatabaseManager.getInstance();
			con = dbMan.borrowConnection();
			Statement stmt = con.createStatement();

			// stmt.executeUpdate(
			// "INSERT INTO CUSTOMER VALUES(50,'Christian','Ullenboom','Immengarten 6','Hannover')" );

			ResultSet rs = stmt.executeQuery("SELECT * FROM Customer");

			while (rs.next())
				System.out.printf("%s, %s %s%n", rs.getString(1), rs.getString(2), rs.getString(3));

			rs.close();

			stmt.close();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dbMan != null && con != null)
			{
				try
				{
					dbMan.returnConnection(con);
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
