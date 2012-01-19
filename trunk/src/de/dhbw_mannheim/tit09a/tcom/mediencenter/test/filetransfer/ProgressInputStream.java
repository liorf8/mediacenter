package de.dhbw_mannheim.tit09a.tcom.mediencenter.test.filetransfer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;


// Similar to FilterInputStream, but it was not necessary to extend the FilterOutputclass.
public class ProgressInputStream extends InputStream
{
    private PropertyChangeListener l;
    private long totalBytesRead;
    private InputStream in;

    public ProgressInputStream(InputStream in, PropertyChangeListener l)
    {
	this.in = in;
	this.l = l;
    }

    public int read() throws IOException
    {
	int nextByte = in.read();
	if (nextByte != -1) // is returned when no more bytes
	    firePropertyChange(1);
	return nextByte;
    }
    
    public int read(byte[] b) throws IOException
    {
	return read(b, 0, b.length);
    }

    public int read(byte[] b, int off, int len) throws IOException
    {
	int bytesRead = in.read(b, off, len);
	if (bytesRead != -1) // is returned when no more bytes
	    firePropertyChange(bytesRead);
	return bytesRead;
    }

    private void firePropertyChange(long bytesRead)
    {
	l.propertyChange(new PropertyChangeEvent(this, "totalBytesRead", totalBytesRead,
		totalBytesRead = totalBytesRead + bytesRead));
    }

}
