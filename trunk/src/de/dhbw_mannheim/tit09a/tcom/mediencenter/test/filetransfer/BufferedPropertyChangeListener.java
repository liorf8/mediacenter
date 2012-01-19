package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class BufferedPropertyChangeListener implements PropertyChangeListener
{
    public static final long DEFAULT_DELAY = 300;
    public static final TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS;

    private final List<PropertyChangeEvent> evts = new ArrayList<PropertyChangeEvent>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    private final Runnable fireEvents = new Runnable()
    {
	public void run()
	{
	    // atomic block
	    synchronized (evts)
	    {
		delayedPropertyChanges(new ArrayList<PropertyChangeEvent>(evts));
		// If in between these two commands the add() method is called,
		// the added event will be cleared by the next line.
		// Because this is not intended, all access to the List 'evts' is synchronized.
		evts.clear();
	    }

	    executed = true;
	}
    };

    private long delay;
    private TimeUnit unit;
    private boolean executed = true;

    public BufferedPropertyChangeListener()
    {
	this(DEFAULT_DELAY, DEFAULT_UNIT);
    }

    public BufferedPropertyChangeListener(long delay, TimeUnit unit)
    {
	this.delay = delay;
	this.unit = unit;
    }

    // "final" so the method does not get overwritten
    @Override
    public final void propertyChange(PropertyChangeEvent evt)
    {
	// evts.add() needs to be synchronized as well
	synchronized (evts)
	{
	    evts.add(evt);
	}

	if (executed == true)
	{
	    executed = false;
	    scheduler.schedule(fireEvents, delay, unit);
	}
    }

    public abstract void delayedPropertyChanges(List<PropertyChangeEvent> evts);
}
