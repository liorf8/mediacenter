package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.scripts;

import java.io.IOException;
import java.sql.SQLException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

public class CreateTables
{

	public static void main(String[] args) throws SQLException, IOException, Exception
	{
		try
		{
			DatabaseManager.getInstance().createTables(true);
			DatabaseManager.getInstance().disConnect();

		}
		finally
		{
			DatabaseManager.getInstance().disConnect();
		}

	}
}
