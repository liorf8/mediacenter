package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.sql.SQLException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class SQLCreateClientUser
{
	public static void main(String[] args) throws Exception
	{
		createClientUser(true);
	}

	public static void createClientUser(boolean drop) throws Exception
	{
		DatabaseManager dbMan = null;
		try
		{
			dbMan = DatabaseManager.getInstance();
			if (drop)
			{
				try
				{
					dbMan.executeStatementAsDBA(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "DropClientUser.sql"), void.class);
				}
				catch (SQLException e)
				{
					System.out.println("Did not need to drop User CLIENT. Seems to have not existed: " +e);
				}
			}
			dbMan.executeStatementAsDBA(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "CreateClientUser.sql"), void.class);
		}
		finally
		{
			if (dbMan != null)
				dbMan.shutdown();
		}
	}

}
