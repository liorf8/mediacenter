package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class MainClass
{
    public class MyPropertyChangeListener implements PropertyChangeListener
    {
	@Override
	public void propertyChange(PropertyChangeEvent evt)
	{
	    if ("totalBytesRead".equals(evt.getPropertyName()))
	    {
		System.out.printf("Old: %d; New: %d Bytes%n", (Long) evt.getOldValue(), (Long) evt.getNewValue());
	    }
	    else if ("progress".equals(evt.getPropertyName()))
	    {
		if ((Integer) evt.getNewValue() % 10 == 0)
		{
		    System.out.printf("Old: %d; New: %d%%%n", (Integer) evt.getOldValue(),
			    (Integer) evt.getNewValue());
		}
	    }
	}
    }

    public static void main(String[] args)
    {
	try
	{
	    InputStream pbis = new ProgressInputStream(
		    new MainClass().new MyPropertyChangeListener(), new File(
			    "D:\\Downloads\\cougar2x20.mkv"));
	    byte[] buffer = new byte[0xFFFF];
	    for (int len; (len = pbis.read(buffer)) != -1;)
	    {

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
    }

}
