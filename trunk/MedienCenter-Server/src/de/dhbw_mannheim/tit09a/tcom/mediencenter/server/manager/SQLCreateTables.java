package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class SQLCreateTables
{
	public static void main(String[] args) throws Exception
	{
		createTables(true);
	}
	
	public static void createTables(boolean drop) throws Exception
	{
		DatabaseManager dbMan = null;
		try
		{
			dbMan = DatabaseManager.getInstance();
			if (drop)
			{
				dbMan.executeStatementAsDBA(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "DropTableUsers.sql"), void.class);
			}
			dbMan.executeStatementAsDBA(IOUtil.resourceToString(DatabaseManager.SQL_STMTS_PATH + "CreateTableUsers.sql"), void.class);
		}
		finally
		{
			if (dbMan != null)
				dbMan.shutdown();
		}
	}
}
