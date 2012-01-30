package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.SwingWorker;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.IntervalPropertyChangeListener;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.ProgressUtil;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.util.TimeValue;

public class ReadInputStreamTask extends SwingWorker<Long, Map<Integer, String>>
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    public static final byte[] DEFAULT_BUFFER = new byte[0xFFFF];
    private static final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1);

    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private final IntervalPropertyChangeListener intervalPCL = new IntervalPropertyChangeListener(
	    executor, 800L, TimeUnit.MILLISECONDS)
    {
	private long lastTimeStamp;
	private long lastTotalBytesRead;

	@Override
	public void propertyChangesAtInterval(List<PropertyChangeEvent> evts)
	{
	    System.out.println("IN propertyChangesAtInterval() @ " + Thread.currentThread());
	    long start = System.currentTimeMillis();
	    System.out.println("Queued: " + executor.getQueue().size());
	    System.out.println("Active: " + executor.getActiveCount());
	    
	    publishMap((Long) evts.get(evts.size() - 1).getNewValue());
	    
	    System.out.println("Duration for publishing: " + (System.currentTimeMillis() - start));
	    System.out.println("OUT propertyChangesAtInterval() @ " + Thread.currentThread());
	}

	@SuppressWarnings("unchecked")
	private void publishMap(long totalBytesRead)
	{
	    long timeStamp = System.currentTimeMillis();

	    int percentage = (int) ProgressUtil.percentage(totalBytesRead, available);
	    float speed = 0f;
	    long remainingSecs = 0;
	    if (lastTimeStamp > 0)
	    {
		speed = ProgressUtil.speed(totalBytesRead - lastTotalBytesRead, timeStamp
			- lastTimeStamp);
		remainingSecs = ProgressUtil.remainingSecs(available - totalBytesRead, speed);
	    }

	    lastTotalBytesRead = totalBytesRead;
	    lastTimeStamp = timeStamp;

	    String string = String.format("%s/%s (ETA %s @ %s/s)", new ByteValue(totalBytesRead),
		    new ByteValue(available), new TimeValue(remainingSecs * 1000), new ByteValue(
			    (int) speed));
	    Map<Integer, String> map = Collections.singletonMap(percentage, string);
	    publish(map);
	}
    };
    // --------------------------------------------------------------------------------
    private InputStream in;
    private byte[] buffer;
    private long available;

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public ReadInputStreamTask(InputStream in, long available)
    {
	this(in, available, DEFAULT_BUFFER);
    }

    // --------------------------------------------------------------------------------
    // -- Public Method(s) ------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public ReadInputStreamTask(InputStream in, long available, byte[] buffer)
    {
	this.in = in;
	this.available = available;
	this.buffer = buffer;
    }

    // --------------------------------------------------------------------------------
    // -- Private/Protected Method(s) -------------------------------------------------
    // --------------------------------------------------------------------------------
    @Override
    protected Long doInBackground() throws Exception
    {
	long totalBytesRead = 0;
	long bytesRead = 0;
	while ((bytesRead = in.read(buffer)) > -1)
	{
	    System.out.println("IN doInBackground() @ " + Thread.currentThread());
	    Thread.sleep(500);
	    totalBytesRead += bytesRead;
	    intervalPCL.propertyChange(new PropertyChangeEvent(this, "totalBytesRead", null,
		    totalBytesRead));
	    System.out.println("OUT doInBackground() @ " + Thread.currentThread());
	}
	return totalBytesRead;
    }

    // --------------------------------------------------------------------------------
    protected void process(List<Map<Integer, String>> values)
    {
	firePropertyChange("progressstatus", null, values.get(values.size() - 1));
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
