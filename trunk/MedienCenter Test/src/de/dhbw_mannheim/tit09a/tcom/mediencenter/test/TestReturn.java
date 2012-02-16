package de.dhbw_mannheim.tit09a.tcom.mediencenter.test;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.DatabaseManager;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.SessionImpl;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ReturnObj;

public class TestReturn
{

	public static void main (String[] args) throws Exception
	{
		new ReturnObj<ServerImpl>(new ServerImpl());
	}
}
