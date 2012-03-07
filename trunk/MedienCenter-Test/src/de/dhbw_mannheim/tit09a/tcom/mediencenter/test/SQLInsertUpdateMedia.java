package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager.NFileManager;

public class SQLInsertUpdateMedia
{
	public static void main(String[] args) throws Exception
	{
		DatabaseManager dbMan = null;
		NFileManager fileMan = null;
		try
		{
			dbMan = DatabaseManager.getInstance();
			fileMan = NFileManager.getInstance();
			fileMan.saveElapsedTime("bla", 5);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			if (dbMan != null)
				dbMan.shutdown();
			if (fileMan != null)
				fileMan.shutdown();
		}

	}

}
