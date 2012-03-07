package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.concurrent;

public class SyncTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new SyncTest();

	}

	private String	s;

	public SyncTest()
	{
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				setString("thread 1");
			}
		}).run();
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				System.out.println(getString());
				eraseString();
				System.out.println(":"+getString());
			}
		}).run();
	}

	private void setString(String s)
	{
		synchronized (s)
		{
			try
			{
				this.s = s;
				Thread.sleep(2000);
			}
			catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void eraseString()
	{
		s = "";
	}

	private String getString()
	{
		return s;
	}

}
