package de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util;

import java.util.concurrent.ThreadFactory;

/**
 * A factory-class that let's you use named threads in a thread-pool
 * 
 * @author achristian
 * 
 *         Max Hertrampf: COPIED but made constructor public. Thx!
 */
public class NamedThreadPoolFactory implements ThreadFactory
{

	/** the base name for each thread created with this factory */
	private String	baseName;

	private long	i	= 0;

	/**
	 * Creates a new thread-factory that gives each thread a basename
	 * 
	 * @param baseName
	 *            the basename for the created threads
	 */
	public NamedThreadPoolFactory(String baseName)
	{
		this.baseName = baseName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	public Thread newThread(Runnable r)
	{
		if ((i++) == Long.MAX_VALUE)
			i = 0;
		StringBuffer sb = new StringBuffer();
		sb.append(baseName);
		sb.append(".#");
		sb.append(i);
		return new Thread(r, sb.toString());
	}

}