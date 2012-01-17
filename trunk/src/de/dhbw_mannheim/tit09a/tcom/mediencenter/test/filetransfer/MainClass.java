package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MainClass
{
    private static JFrame frame;
    private static JPanel panel;
    private static JProgressBar bar;

    public class MyPropertyChangeListener implements PropertyChangeListener
    {
	private int progress;
	private long totalBytesRead;
	private long fileSize;
	private long timeStamp = System.currentTimeMillis();
	private ByteValue speed = new ByteValue(0);

	private void setString()
	{
	    bar.setString(String.format("%d%% (%s/%s, Speed: %s/s)", progress, new ByteValue(
		    totalBytesRead).toString(), new ByteValue(fileSize).toString(), speed));
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
	    if ("totalBytesRead".equals(evt.getPropertyName()))
	    {
		if ((System.currentTimeMillis() - timeStamp) > 500)
		{
		    long byteDiff = -1
			    * (totalBytesRead - (totalBytesRead = (Long) evt.getNewValue()));
		    long timeDiff = -1 * (timeStamp - (timeStamp = System.currentTimeMillis()));
		    speed.setBytes((int) (byteDiff / (timeDiff / 1000f)));

		    // System.out.printf("Old: %d; New: %d (read:%d) Bytes%n", totalBytesRead, totalBytesRead
		    // =
		    // (Long) evt.getNewValue(),totalBytesRead - (Long) evt.getOldValue());
		    setString();
		}
	    }
	    else if ("progress".equals(evt.getPropertyName()))
	    {
		progress = (Integer) evt.getNewValue();
		// System.out.printf("Old: %d; New: %d%%%n", progress, progress = (Integer)
		// evt.getNewValue());
		setString();
		bar.setValue(progress);
	    }
	    else if ("file".equals(evt.getPropertyName()))
	    {
		System.out.printf("File changed: Old: %s, New: %s%n", fileSize,
			fileSize = ((File) evt.getNewValue()).length());
		setString();
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
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    SwingUtilities.invokeAndWait(new Runnable()
	    {
		public void run()
		{
		    buildGUI();
		}
	    });
	    String filepath = "C:\\Users\\Max\\Videos\\!Filme\\rsg-die frau des zeitreisenden-720p.mkv";

	    PropertyChangeListener pcl = new MainClass().new MyPropertyChangeListener();
	    InputStream pbis = new ProgressInputStream(pcl, new File(filepath));

	    byte[] buffer = new byte[0xFFFF];
	    // 0xFFFF = 64kb (65535Bytes)
	    while (pbis.read(buffer) != -1)
	    {
		// try
		// {
		// Thread.sleep(100);
		// }
		// catch (InterruptedException ignore)
		// {
		// }
	    }
	}
	catch (FileNotFoundException e)
	{
	    e.printStackTrace();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
	catch (InterruptedException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (InvocationTargetException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (ClassNotFoundException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (InstantiationException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (IllegalAccessException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	catch (UnsupportedLookAndFeelException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    private static void buildGUI()
    {
	bar = new JProgressBar();
	bar.setMinimumSize(new Dimension(400, 200));
	bar.setStringPainted(true);

	panel = new JPanel();
	panel.setLayout(new GridLayout(1, 1));
	panel.add(bar);

	frame = new JFrame("ReadFile");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(400, 200);
	frame.setLocation(300, 300);
	frame.add(panel);

	frame.pack();
	frame.setVisible(true);
    }

}
