package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BufferedPropertyChangeListener_oldWithTimerTask implements PropertyChangeListener
{
    public static final int DEFAULT_DELAY = 300;
    private final List<PropertyChangeEvent> evts;
    private final Timer timer;
    private int delay;
    private boolean executed = true;

    public BufferedPropertyChangeListener_oldWithTimerTask()
    {
        this(DEFAULT_DELAY);
    }

    public BufferedPropertyChangeListener_oldWithTimerTask(int delay)
    {
        this.delay = delay;
        timer = new Timer();
        evts = new ArrayList<PropertyChangeEvent>();
    }

    public int getDelay()
    {
        return delay;
    }
    
    public void setDelay(int delay)
    {
        this.delay = delay;
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
            timer.schedule(new FireEventsTask(), delay);
        }
    }

    private void fireEvents()
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
    
    public abstract void delayedPropertyChange(List<PropertyChangeEvent> evts);
    
    private class FireEventsTask extends TimerTask
    {
        public void run()
        {
            fireEvents();
        }
    }
}
