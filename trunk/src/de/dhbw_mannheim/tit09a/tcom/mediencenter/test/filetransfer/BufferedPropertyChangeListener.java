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
    private final List<PropertyChangeEvent> evts;
    private final ScheduledExecutorService scheduler;
    private final Runnable fireEvents;
    private long delay;
    private TimeUnit unit;
    private boolean executed = true;

    public BufferedPropertyChangeListener()
    {
	this(DEFAULT_DELAY, DEFAULT_UNIT);
    }

    public BufferedPropertyChangeListener(long delay, TimeUnit unit)
    {
	evts = new ArrayList<PropertyChangeEvent>();
	scheduler = Executors.newSingleThreadScheduledExecutor();
	fireEvents = new Runnable()
	{
	    public void run()
	    {
		synchronized (evts)
		{
		    // atomic block
		    delayedPropertyChange(new ArrayList<PropertyChangeEvent>(evts));
		    // If in between these two commands the add() method is called,
		    // the added event will be cleared by the next line.
		    // Because this is not intended, all access to the List is synchronized by this method.
		    evts.clear();
		}
		executed = true;
	    }
	};
	this.delay = delay;
	this.unit = unit;
    }

    // "final" so the method does not get overwritten
    @Override
    public final void propertyChange(PropertyChangeEvent evt)
    {
	// add() needs to be synchronized as well
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

    public abstract void delayedPropertyChange(List<PropertyChangeEvent> evts);
}
