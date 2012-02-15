package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

public class CreateTest
{

	public static void main (String[] args)
	{
		try
		{
			DatabaseManager.getInstance().createTables(true);
			DatabaseManager.getInstance().closeConnection();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}
}
