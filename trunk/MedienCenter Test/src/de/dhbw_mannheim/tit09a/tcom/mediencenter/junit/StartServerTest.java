package de.dhbw_mannheim.tit09a.tcom.mediencenter.junit;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.server.ServerMain;

public class StartServerTest
{

	@Before
	public void setUp() throws Exception
	{}

	@After
	public void tearDown() throws Exception
	{}

	@Test
	public void testServerStart()
	{
		try
		{
			ServerMain.start();
			ServerMain.shutdown();
			ServerMain.start();
			ServerMain.restart();
			ServerMain.exit();
		}
		catch (Exception e)
		{
			fail(e.toString());
		}
	}
}
