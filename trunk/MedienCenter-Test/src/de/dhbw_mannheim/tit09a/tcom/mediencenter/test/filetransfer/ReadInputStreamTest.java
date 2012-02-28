package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ReadInputStreamTest
{
    // --------------------------------------------------------------------------------
    // -- Class Variable(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------

    // --------------------------------------------------------------------------------
    // -- Instance Variable(s) --------------------------------------------------------
    // --------------------------------------------------------------------------------
    private JFrame frame;
    private JPanel panel;
    private JProgressBar bar;
    private long fileSize;

    // --------------------------------------------------------------------------------
    // -- Constructor(s) --------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public ReadInputStreamTest() throws Exception
    {
	// --------------------------------------------------------------------------------
	// -- 1. Build the GUI
	SwingUtilities.invokeAndWait(new Runnable()
	{
	    public void run()
	    {
		buildGUI();
	    }
	});

	// --------------------------------------------------------------------------------
	// -- 2. Read the file
	File file = new File("D:\\Downloads\\jeeperscreepers.mkv");
	fileSize = file.length();
	ReadInputStreamTask task = new ReadInputStreamTask(new FileInputStream(file), fileSize);
	task.addPropertyChangeListener(new PropertyChangeListener()
	{
	    @Override
	    public void propertyChange(PropertyChangeEvent evt)
	    {
		System.out.println("IN propertyChange() @ " + Thread.currentThread());
		long start = System.currentTimeMillis();

		if ("progressstatus".equals(evt.getPropertyName()))
		{
		    @SuppressWarnings("unchecked")
		    Map<Integer, String> map = (Map<Integer, String>) evt.getNewValue();
		    for (Entry<Integer, String> entry : map.entrySet())
		    {
			setBar(entry.getKey(), entry.getValue());
		    }

		}
		System.out.println("Duration for GUI update: "
			+ (System.currentTimeMillis() - start));
		System.out.println("OUT propertyChange() @ " + Thread.currentThread());
	    }
	});
	task.execute();
	
	//ReadInputStreamTask task2 = new ReadInputStreamTask(new FileInputStream(file), fileSize);
	//task2.execute();

	// --------------------------------------------------------------------------------
	// -- 3. Print the result
	System.out.println(task.get());

    }

    // --------------------------------------------------------------------------------
    // -- Private Method(s) -----------------------------------------------------------
    // --------------------------------------------------------------------------------
    private void setBar(int newValue, String newString)
    {
	System.out.println(" setBar() @ " + Thread.currentThread());
	if (bar.isIndeterminate())
	{
	    bar.setIndeterminate(false);
	}
	bar.setValue(newValue);
	bar.setString(newString);
    }

    // --------------------------------------------------------------------------------
    private void buildGUI()
    {
	System.out.println("Building GUI @ " + Thread.currentThread());
	bar = new JProgressBar();
	bar.setMinimumSize(new Dimension(350, 180));
	bar.setSize(350, 180);
	bar.setStringPainted(true);
	bar.setIndeterminate(true);

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

    // --------------------------------------------------------------------------------
    // -- Main Method -----------------------------------------------------------------
    // --------------------------------------------------------------------------------
    public static void main(String[] args)
    {
	try
	{
	    new ReadInputStreamTest();
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
    }

    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
    // --------------------------------------------------------------------------------
}
