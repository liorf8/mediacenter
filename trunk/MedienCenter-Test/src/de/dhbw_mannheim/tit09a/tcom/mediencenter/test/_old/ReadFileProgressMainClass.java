package de.dhbw_mannheim.tit09a.tcom.mediencenter.test._old;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.ByteValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.shared.misc.TimeValue;
import de.dhbw_mannheim.tit09a.tcom.mediencenter.test.util.IntervalPropertyChangeListener;

public class ReadFileProgressMainClass
{
    private static JFrame frame;
    private static JPanel panel;
    private static JProgressBar bar;
    private static long fileSize;

    public class MyPropertyChangeListener extends IntervalPropertyChangeListener
    {
	public MyPropertyChangeListener()
	{
	    super();
	}

	private int progress;
	private long totalBytesRead;
	private long timeStamp = System.currentTimeMillis();
	private ByteValue speed = new ByteValue(0);
	private long timeRemaining;
	private long byteDiff;
	private long timeDiff;

	private void setString()
	{
	   // System.out.print("Change GUI @ " +Thread.currentThread());
	    bar.setString(String.format("%d%% - %s/%s (ETA %s @ %s/s)", progress, new ByteValue(
		    totalBytesRead).toString(), new ByteValue(fileSize).toString(),
		    TimeValue.formatMillis(timeRemaining * 1000, true, true), speed));
	}

	private void handlePropertyChange(PropertyChangeEvent evt)
	{
//	    System.out.print("Processing Events @ " +Thread.currentThread());
//	    System.out.println("=>" + evt.getNewValue() + ";");
	    // speed
	    byteDiff = -1 * (totalBytesRead - (totalBytesRead = (Long) evt.getNewValue()));
	    timeDiff = -1 * (timeStamp - (timeStamp = System.currentTimeMillis()));
	    speed.setBytes((int) (byteDiff / (timeDiff / 1000f)));
	    timeRemaining = (fileSize - totalBytesRead) / speed.getBytes();

	    // progress
	    progress = (int) (totalBytesRead * 100 / fileSize);
	    bar.setValue(progress);

	    SwingUtilities.invokeLater(new Runnable()
	    {
		public void run()
		{
		    setString();
		}
	    });
	}

	@Override
	public void propertyChangesAtInterval(List<PropertyChangeEvent> evts)
	{
	    System.out.print("Processing Events @ " +Thread.currentThread());
	    for (PropertyChangeEvent evt : evts)
	    {
		System.out.print(evt.getNewValue() + ";");
	    }
	    System.out.println();
	    PropertyChangeEvent evt = evts.get(evts.size() - 1);

	    if ("totalBytesRead".equals(evt.getPropertyName()))
	    {
		if ((Long) evt.getNewValue() > -1)
		{
		    handlePropertyChange(evt);
		}
		else
		{
		    System.out.println("Finished!");
		    if (evts.size() > 1) // is there any Evt before?
		    {
			evt = evts.get(evts.size() - 2);
			handlePropertyChange(evt);
		    }
		}
	    }
	    else
	    {
		System.out.println("Unknown property change:" + evt.getPropertyName());
	    }
	}
    }

    public static void main(String[] args)
    {
	try
	{
	    System.out.println("Invoking Main @ " + Thread.currentThread());
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    SwingUtilities.invokeAndWait(new Runnable()
	    {
		public void run()
		{
		    buildGUI();
		}
	    });

	    String filename = "D:\\mhertram\\Downloads\\eclipse-jee-indigo-SR1-win32-x86_64.zip";
	    if (args.length > 0)
	    {
		filename = args[0];
	    }
	    File file = new File(filename);

	    PropertyChangeListener pcl = new ReadFileProgressMainClass().new MyPropertyChangeListener();
	    InputStream pbis = new ProgressInputStream(new FileInputStream(file), pcl);
	    fileSize = file.length();
	    System.out.println(fileSize);

	    byte[] buffer = new byte[0xFFFF];
	    // 0xFFFF = 64kb (65535Bytes)
	    while (pbis.read(buffer) != -1)
	    {
		try
		{
		    Thread.sleep(100);
		}
		catch (InterruptedException ignore)
		{
		}
	    }
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    private static void buildGUI()
    {
	System.out.println("Building GUI @ " +Thread.currentThread());
	bar = new JProgressBar();
	bar.setMinimumSize(new Dimension(350, 180));
	bar.setSize(350, 180);
	bar.setStringPainted(true);

	panel = new JPanel();
	panel.setLayout(new GridLayout(1, 1));
	panel.add(bar);

	frame = new JFrame("ReadFile");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(400, 200);
	bar.setMinimumSize(new Dimension(400, 200));
	frame.setLocation(300, 300);
	frame.add(panel);

	frame.pack();
	frame.setVisible(true);
    }

}
