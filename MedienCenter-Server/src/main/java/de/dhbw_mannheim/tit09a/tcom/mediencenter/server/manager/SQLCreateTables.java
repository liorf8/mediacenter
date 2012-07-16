package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

public class SQLCreateTables
{
	public static void main(String[] args) throws Exception
	{
		createTables(true);
	}

	public static void createTables(boolean drop) throws Exception
	{
		if (drop)
		{
			DatabaseManager.executeStatementAsDBA(DatabaseManager.getSQL("DropTableUsers.sql"), void.class);
			DatabaseManager.executeStatementAsDBA(DatabaseManager.getSQL("DropTableMedia.sql"), void.class);
		}
		DatabaseManager.executeStatementAsDBA(DatabaseManager.getSQL("CreateTableUsers.sql"), void.class);
		DatabaseManager.executeStatementAsDBA(DatabaseManager.getSQL("CreateTableMedia.sql"), void.class);
		
		System.out.println("done");
	}
}
