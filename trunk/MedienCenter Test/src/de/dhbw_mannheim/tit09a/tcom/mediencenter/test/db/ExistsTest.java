package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.UserManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.Manager;

public class ExistsTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println(Manager.getManager(UserManager.class).userExists(Manager.getManager(DatabaseManager.class).getConnection(), "Max1"));
		
		System.out.println(Manager.getManager(UserManager.class).authenticate(Manager.getManager(DatabaseManager.class).getConnection(), 99, "pw"));
	}

}
