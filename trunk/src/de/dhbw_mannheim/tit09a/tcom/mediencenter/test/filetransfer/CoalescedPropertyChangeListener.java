package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class CoalescedPropertyChangeListener implements PropertyChangeListener
{
    public static final int DEFAULT_DELAY = 100;

    private final List<PropertyChangeEvent> evts;
    private int delay;
    private long lastFireTime;
    private Timer timer;

    public CoalescedPropertyChangeListener()
    {
	this(DEFAULT_DELAY);
    }

    public CoalescedPropertyChangeListener(int delay)
    {
	evts = new ArrayList<PropertyChangeEvent>();
	this.delay = delay;
    }

    class FireEventsTask extends TimerTask
    {
	public void run()
	{
	    System.out.println(new Date()+": Fired by Timer!");
	    firePropertyChange();
	}
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt)
    {
	evts.add(evt);

	if (timer == null)
	{
	    timer = new Timer();
	    timer.schedule(new FireEventsTask(), delay+100);
	}

	if (System.currentTimeMillis() - lastFireTime > delay) // delay elapsed
	{
	    System.out.println(new Date()+": Fired BEFORE Timer!");
	    firePropertyChange();
	    timer.cancel();
	    timer = null;
	}
    }

    private void firePropertyChange()
    {
	delayedPropertyChange(new ArrayList<PropertyChangeEvent>(evts));
	evts.clear();
	lastFireTime = System.currentTimeMillis();
    }

    public abstract void delayedPropertyChange(List<PropertyChangeEvent> evts);

}
