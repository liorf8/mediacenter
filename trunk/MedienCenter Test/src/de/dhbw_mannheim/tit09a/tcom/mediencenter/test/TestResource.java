package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import java.io.IOException;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.util.IOUtil;

public class TestResource
{
	private static final String	SQL_STMT_PATH	= "/de/dhbw_mannheim/tit09a/tcom/mediencenter/server/sql/";

	public static void main(String[] args) throws IOException
	{
		String createTableUsers = IOUtil.convertStreamToString(TestResource.class.getResourceAsStream(SQL_STMT_PATH + "CreateTableUsers.sql"));
		System.out.println(createTableUsers);
	}

}
