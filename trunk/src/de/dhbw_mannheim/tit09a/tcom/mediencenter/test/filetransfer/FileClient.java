package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.net.*;
import java.util.List;
import java.beans.PropertyChangeEvent;
import java.io.*;

public class FileClient
{
    public static void main(String[] args) throws IOException
    {
	long start = System.currentTimeMillis();
	// localhost for testing
	System.out.println("Connecting...");
	Socket sock = new Socket("127.0.0.1", 13267);
	
	System.out.println("getReceiveBufferSize(): " +sock.getReceiveBufferSize());
	System.out.println("getSendBufferSize(): " +sock.getSendBufferSize());
	
	// receive file
	byte[] buffer = new byte[8192];
	InputStream is = new ProgressInputStream(sock.getInputStream(),
		new FileClient().new InputStreamProgressListener());

	while (is.read(buffer) > -1)
	{

	}

	long end = System.currentTimeMillis();
	System.out.println("Duration: " + (end - start));
	sock.close();
    }

    private class InputStreamProgressListener extends BufferedPropertyChangeListener
    {
	private PropertyChangeEvent evt;

	@Override
	public void delayedPropertyChanges(List<PropertyChangeEvent> evts)
	{
//	    System.out.print("Events:");
//	    for (PropertyChangeEvent evt : evts)
//	    {
//		System.out.print(evt.getNewValue() + ";");
//	    }
//	    System.out.println();
	    evt = evts.get(evts.size() - 1);
	    System.out.println("=>" + evt.getNewValue() + ";");

	}

    }
}