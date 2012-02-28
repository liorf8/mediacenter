package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class IntervalPropertyChangeListener implements PropertyChangeListener
{
	// --------------------------------------------------------------------------------
	// -- Class Variable(s) -----------------------------------------------------------
	// --------------------------------------------------------------------------------
	public static final long				DEFAULT_INTERVAL	= 300;
	public static final TimeUnit			DEFAULT_UNIT		= TimeUnit.MILLISECONDS;

	// --------------------------------------------------------------------------------
	// -- Instance Variable(s) --------------------------------------------------------
	// --------------------------------------------------------------------------------
	private final List<PropertyChangeEvent>	evts				= new ArrayList<PropertyChangeEvent>();
	private final Runnable					fireEvents			= new Runnable()
																{
																	public void run()
																	{
																		// System.out.println("Time's up ("+delay+"). Firing Events @" +
																		// Thread.currentThread());
																		// atomic block
																		synchronized (evts)
																		{
																			propertyChangesAtInterval(new ArrayList<PropertyChangeEvent>(evts));
																			// If in between these two commands the add() method is called,
																			// the added event will be cleared by the next line.
																			// Because this is not intended, all access to the List 'evts' is
																			// synchronized.
																			evts.clear();
																		}

																		executed = true;
																	}
																};
	// --------------------------------------------------------------------------------
	private ScheduledExecutorService		scheduler;
	private long							interval;
	private TimeUnit						unit;
	private boolean							executed			= true;

	// --------------------------------------------------------------------------------
	// -- Constructor(s) --------------------------------------------------------------
	// --------------------------------------------------------------------------------
	public IntervalPropertyChangeListener()
	{
		this(null, 0, null);
	}

	// --------------------------------------------------------------------------------
	public IntervalPropertyChangeListener(ScheduledExecutorService scheduler, long interval, TimeUnit unit)
	{
		if (scheduler == null)
		{
			// this.scheduler = new ScheduledThreadPoolExecutor(1);
			this.scheduler = Executors.newSingleThreadScheduledExecutor();
		}
		else
		{
			this.scheduler = scheduler;
		}

		if (interval > 0)
		{
			this.interval = interval;
		}
		else if (interval == 0)
		{
			this.interval = DEFAULT_INTERVAL;
		}
		else
		// < 0
		{
			throw new IllegalArgumentException("Interval must be >= 0 (0 to use default).");
		}

		if (unit == null)
		{
			this.unit = DEFAULT_UNIT;
		}
		else
		{
			this.unit = unit;
		}
	}

	// --------------------------------------------------------------------------------
	// -- Public Method(s) ------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// "final" so the method does not get overwritten
	@Override
	public final void propertyChange(PropertyChangeEvent evt)
	{
		if (executed == true)
		{
			executed = false;
			scheduler.schedule(fireEvents, interval, unit);
		}

		// evts.add() needs to be synchronized as well
		synchronized (evts)
		{
			evts.add(evt);
		}
	}

	// --------------------------------------------------------------------------------
	// This Method needs to be overwritten
	public abstract void propertyChangesAtInterval(List<PropertyChangeEvent> evts);

	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
	// --------------------------------------------------------------------------------
}
