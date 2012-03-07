package de.dhbw_mannheim.tit09a.tcom.mediencenter.server.manager;

import java.sql.SQLException;

public class SQLCreateClientUser
{
	public static void main(String[] args) throws Exception
	{
		createClientUser(true);
	}

	public static void createClientUser(boolean drop) throws Exception
	{
		if (drop)
		{
			try
			{
				DatabaseManager.executeStatementAsDBA(DatabaseManager.getSQL("DropClientUser.sql"), void.class);
			}
			catch (SQLException e)
			{
				System.out.println("Did not need to drop User CLIENT. Seems to have not existed: " + e);
			}
		}
		DatabaseManager.executeStatementAsDBA(DatabaseManager.getSQL("CreateClientUser.sql"), void.class);
		
		System.out.println("done");
	}
}
