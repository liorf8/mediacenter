package de.dhbw_mannheim.tit09a.tcom.mediencenter.junit;

import static org.junit.Assert.*;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringReader;

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
		Runnable myRunnable = new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println("Running run() on " +Thread.currentThread());
				try
				{
					ServerMain.main(new String[] {});
				}
				catch (Exception e)
				{
					fail(e.toString());
				}
			}

		};
		Thread myThread = new Thread(myRunnable);
		myThread.run();

		System.out.println("Running on " +Thread.currentThread());
		ServerMain.exit();

	}

}
