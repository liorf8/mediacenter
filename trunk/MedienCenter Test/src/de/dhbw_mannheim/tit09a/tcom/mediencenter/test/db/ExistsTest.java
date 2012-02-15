package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.db;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.Authenticator;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;

public class ExistsTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		System.out.println(Authenticator.getInstance().userExists(DatabaseManager.getInstance().getConnection(), "Max"));
		
		System.out.println(Authenticator.getInstance().authenticate(DatabaseManager.getInstance().getConnection(), "Max", "pw"));
	}

}
