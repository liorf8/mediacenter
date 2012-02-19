package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.scripts;

import java.io.IOException;
import java.sql.SQLException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.Manager;

public class CreateTables
{

	public static void main(String[] args) throws SQLException, IOException, Exception
	{
		DatabaseManager dbMan = null;
		try
		{
			dbMan = Manager.getManager(DatabaseManager.class);
			dbMan.createTables(true);

		}
		finally
		{
			if(dbMan != null)
				dbMan.disConnect();
		}

	}
}
